package com.hrushko.command.action;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.service.ServiceException;
import com.hrushko.service.UserService;
import com.hrushko.service.impl.UserServiceImpl;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteUserCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(DeleteUserCommand.class);

    private static final String USER_ID = "userId";
    private static final String ERROR_DONT_FIND_USER = "error_dont_find_user";
    private static final String DELETE_SUCCESS = "user.delete.success";
    private static final String DELETE_FAIL = "user.delete.fail";
    private static final String DONE = "done";
    private static final String ERROR = "error";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        if (request.getParameter(USER_ID) == null) {
            LOGGER.log(Level.WARN, ERROR_DONT_FIND_USER);
            return failure(ERROR_DONT_FIND_USER, request);
        }
        Integer userId;
        try {
            userId = Integer.valueOf(request.getParameter(USER_ID));
            deleteUser(userId);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARN, ERROR_DONT_FIND_USER);
            return failure(ERROR_DONT_FIND_USER, request);
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, e.getMessage());
            return failure(DELETE_FAIL, request);
        }
        request.getSession().setAttribute(DONE, DELETE_SUCCESS);
        return new CommandResult(ResourceManager.getProperty("command.allUsers"), true);
    }

    private void deleteUser(Integer userId) throws ServiceException {
        UserService service = new UserServiceImpl();
        service.delete(userId);
    }

    private CommandResult failure(String error, HttpServletRequest request) {
        request.getSession().setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("command.allUsers"), true);
    }
}
