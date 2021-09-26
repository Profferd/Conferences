package com.hrushko.command.action;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.entity.Permission;
import com.hrushko.entity.User;
import com.hrushko.entity.UserInfo;
import com.hrushko.service.PermissionService;
import com.hrushko.service.ServiceException;
import com.hrushko.service.UserInfoService;
import com.hrushko.service.UserService;
import com.hrushko.service.impl.PermissionServiceImpl;
import com.hrushko.service.impl.UserInfoServiceImpl;
import com.hrushko.service.impl.UserServiceImpl;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllUsersCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(AllUsersCommand.class);

    private static final String USERS = "users";
    private static final String USERS_INFO = "users_info";
    private static final String ERROR_FIND_USERS = "error_find_users";
    private static final String ERROR_FIND_USERS_INFO = "error_find_users_info";
    private static final String ERROR = "error";
    private static final String ERROR_FIND_THEME = "error_find_theme";
    private static final String PERMISSION = "permission";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        List<User> users = null;
        try {
            users = giveUsers();
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, ERROR_FIND_USERS);
            return failure(ERROR_FIND_USERS, request);
        }

        Map<Integer, UserInfo> usersInfo;
        try {
            usersInfo = giveUsersInfo(users);
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, ERROR_FIND_USERS_INFO);
            return failure(ERROR_FIND_USERS_INFO, request);
        }
        List<Permission> permissions;
        try {
            permissions = readPermissions();
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, ERROR_FIND_THEME);
            return failure(ERROR_FIND_THEME, request);
        }

        request.getSession().setAttribute(PERMISSION, permissions);
        request.getSession().setAttribute(USERS, users);
        request.getSession().setAttribute(USERS_INFO, usersInfo);
        request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.allUsers"));
        return new CommandResult(ResourceManager.getProperty("page.main"));
    }

    private List<Permission> readPermissions() throws ServiceException {
        PermissionService service = new PermissionServiceImpl();
        return service.readAll();
    }

    private Map<Integer, UserInfo> giveUsersInfo(List<User> users) throws ServiceException {
        Map<Integer, UserInfo> usersInfo = new HashMap<>();
        UserInfoService service = new UserInfoServiceImpl();
        for (User user : users) {
            usersInfo.put(user.getId(), service.findByUser(user));
        }
        return usersInfo;
    }

    private List<User> giveUsers() throws ServiceException {
        UserService service = new UserServiceImpl();
        return service.findAll();
    }

    private CommandResult failure(String error, HttpServletRequest request) {
        request.setAttribute(ERROR, error);
        request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.allUsers"));
        return new CommandResult(ResourceManager.getProperty("page.main"));
    }
}
