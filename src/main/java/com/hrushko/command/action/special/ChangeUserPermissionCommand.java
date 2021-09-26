package com.hrushko.command.action.special;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.Permission;
import com.hrushko.entity.User;
import com.hrushko.service.ServiceException;
import com.hrushko.service.UserService;
import com.hrushko.service.impl.UserServiceImpl;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeUserPermissionCommand implements Command {
    private static final String USER_ID = "userId";
    private static final String INVALID_INPUT = "invalid.input";
    private static final String PERMISSION_UPDATE_FAIL = "permission.update.fail";
    private static final String ERROR = "error";
    private static final String PERMISSION_UPDATE_SUCCESS = "permission.update.success";
    private static final String DONE = "done";
    private static final Logger logger = LogManager.getLogger(ChangeUserPermissionCommand.class);
    private static final String PERMISSION_ID = "permissionId";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String permissionString = request.getParameter(PERMISSION_ID);
        String userIdString = request.getParameter(USER_ID);

        Integer permissionId;
        Integer userId;
        try {
            permissionId = Integer.valueOf(permissionString);
            userId = Integer.valueOf(userIdString);
        } catch (NumberFormatException e) {
            logger.log(Level.WARN, "Invalid parameters!");
            return failure(request, INVALID_INPUT);
        }
        try {
            User user = findUser(userId);
            Permission newPermission = new Permission();
            newPermission.setId(permissionId);
            user.setPermission(newPermission);
            updateUser(user);
        } catch (ServiceException e) {
            logger.log(Level.WARN, PERMISSION_UPDATE_FAIL);
            return failure(request, PERMISSION_UPDATE_FAIL);
        }
        request.getSession().setAttribute(DONE, PERMISSION_UPDATE_SUCCESS);
        return new CommandResult(ResourceManager.getProperty("command.allUsers"), true);
    }

    private void updateUser(User user) throws ServiceException {
        UserService service = new UserServiceImpl();
        service.save(user);
    }

    private User findUser(Integer userId) throws ServiceException {
        UserService service = new UserServiceImpl();
        return service.findById(userId);
    }

    private CommandResult failure(HttpServletRequest request, String error) {
        request.getSession().setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("command.allUsers"), true);
    }
}
