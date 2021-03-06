package com.hrushko.command.action.authorization;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.*;
import com.hrushko.filter.AccessSystem;
import com.hrushko.service.ServiceException;
import com.hrushko.service.UserInfoService;
import com.hrushko.service.UserService;
import com.hrushko.service.impl.UserInfoServiceImpl;
import com.hrushko.service.impl.UserServiceImpl;
import com.hrushko.util.ResourceManager;
import com.hrushko.util.menu.MenuCreator;
import com.hrushko.validate.UserInfoValidator;
import com.hrushko.validate.UserValidator;
import com.hrushko.validate.Validator;
import com.hrushko.validate.ValidatorException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.hrushko.util.Constances.*;

public class RegisterCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(RegisterCommand.class);
    private static final String ERROR = "error";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String EMAIL = "email";
    private static final String ERROR_REGISTRATION = "registration.error";
    private static final String FIELDS_INVALID = "fields.invalid";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(LOGIN, request.getParameter(LOGIN));
        parameters.put(PASSWORD, request.getParameter(PASSWORD));
        parameters.put(NAME, request.getParameter(NAME));
        parameters.put(SURNAME, request.getParameter(SURNAME));
        parameters.put(EMAIL, request.getParameter(EMAIL));

        for (Map.Entry<String, String> entry:
                parameters.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                LOGGER.log(Level.ERROR, "invalid " + entry.getKey() + " was receive");
                return failure(request, FIELDS_INVALID);
            }
        }

        boolean userExist;
        try {
            userExist = checkIfUserExist(parameters.get(LOGIN));
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, e.getMessage());
            return failure(request, e.getMessage());
        }
        if (userExist) {
            LOGGER.log(Level.WARN, "User with such login and password already exist");
            return failure(request, ERROR_REGISTRATION);
        }
        try {
            createUser(parameters, request);
            LOGGER.log(Level.INFO, "User registered and authorized with login - " + parameters.get(LOGIN));
            return new CommandResult(ResourceManager.getProperty("command.home"), true);
        } catch (ValidatorException | ServiceException e) {
            LOGGER.log(Level.WARN, e.getMessage());
            return failure(request, e.getMessage());
        }
    }

    private boolean checkIfUserExist(String login) throws ServiceException {
        UserService service = new UserServiceImpl();
        return service.isExist(login);
    }

    private void createUser(Map<String, String> parameters, HttpServletRequest request) throws ValidatorException, ServiceException {
        User user = new User();
        user.setLogin(parameters.get(LOGIN));
        user.setPassword(parameters.get(PASSWORD));
        Permission permission = new Permission();
        permission.setId(BasePermission.USER);
        user.setPermission(permission);

        UserInfo info = new UserInfo();
        info.setUser(user);
        info.setName(parameters.get(NAME));
        info.setSurname(parameters.get(SURNAME));
        info.setEmail(parameters.get(EMAIL));
        info.setDateOfRegistration(new Date());
        info.setDateOfBirth(new Date());

        if (userValid(user) && infoValid(info)) {
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            UserService userService = new UserServiceImpl();
            Integer id = userService.save(user);
            if (id != null) {
                user.setId(id);
                UserInfoService infoService = new UserInfoServiceImpl();
                infoService.save(info);
            } else {
                throw new ServiceException("Can't save user!");
            }
        }
        setAttributesToSession(request, user);
    }

    private boolean userValid(User user) throws ValidatorException {
        Validator validator = new UserValidator();
        String error = validator.isValid(user);
        if (error != null) {
            throw new ValidatorException(FIELDS_INVALID);
        } else {
            return true;
        }
    }

    private boolean infoValid(UserInfo info) throws ValidatorException {
        Validator validator = new UserInfoValidator();
        String error = validator.isValid(info);
        if (error != null) {
            throw new ValidatorException(error);
        } else {
            return true;
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
        return new CommandResult(ResourceManager.getProperty("command.registerPage"), true);
    }
}
