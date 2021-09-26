package com.hrushko.command.action.authorization;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.User;
import com.hrushko.entity.UserDto;
import com.hrushko.filter.AccessSystem;
import com.hrushko.service.ServiceException;
import com.hrushko.service.UserService;
import com.hrushko.service.impl.UserServiceImpl;
import com.hrushko.util.ResourceManager;
import com.hrushko.util.menu.MenuCreator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.hrushko.util.Constances.*;

public class LoginCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String USER_FIND_ERROR = "user.find.error";
    private static final String ERROR = "error";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        if (login == null && login.isEmpty()) {
            LOGGER.log(Level.WARN, "invalid login was received");
            return failure(request, USER_FIND_ERROR);
        }
        if (password == null || password.isEmpty()) {
            return failure(request, USER_FIND_ERROR);
        }
        boolean userExist = false;
        try {
            userExist = initializeUser(login, password, request);
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, e.getMessage());
            return failure(request, e.getMessage());
        }
        if (userExist) {
            LOGGER.log(Level.WARN, "User authorized with login - " + login);
            return new CommandResult(ResourceManager.getProperty("command.home"), true);
        } else {
            LOGGER.log(Level.WARN, "User with such login and password doesn't exist");
            return failure(request, USER_FIND_ERROR);
        }
    }

    private boolean initializeUser(String login, String password, HttpServletRequest servletRequest) throws ServiceException {
        UserService service = new UserServiceImpl();
        User user = service.findByLoginAndPassword(login, password);
        if (user != null && user.getId() != null) {
            setAttributesToSession(servletRequest, user);
            return true;
        } else {
            return false;
        }
    }

    private void setAttributesToSession(HttpServletRequest request, User user) {
        UserDto userDto = new UserDto(user);
        request.getSession().setAttribute(USER.getFieldName(), userDto);
        request.getSession().setAttribute(PERMISSION.getFieldName(), user.getPermission());
        request.getSession().setAttribute(MENU.getFieldName(), MenuCreator.getMenuItems(user.getPermission()));
        AccessSystem.updateRules(userDto);
    }

    private CommandResult failure(HttpServletRequest request, String error) {
        request.getSession().setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("command.loginPage"), true);
    }
}
