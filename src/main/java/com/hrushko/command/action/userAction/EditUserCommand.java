package com.hrushko.command.action.userAction;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.User;
import com.hrushko.entity.UserDto;
import com.hrushko.service.ServiceException;
import com.hrushko.service.UserService;
import com.hrushko.service.impl.UserServiceImpl;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import com.hrushko.validate.UserValidator;
import com.hrushko.validate.Validator;
import com.hrushko.validate.ValidatorException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditUserCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(EditUserCommand.class);
    private static final String ERROR = "error";
    private static final String USER_EDIT_SUCCESS = "user.edit.success";
    private static final String ERROR_UPDATE_USER = "error_update_user";
    private static final String VERIFY_PASSWORD_IS_INCORRECT = "verify_password_is_incorrect";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String REPEAT_PASSWORD = "password2";
    private static final String DONE = "done";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        UserDto userDto = (UserDto) request.getSession().getAttribute(Constances.USER.getFieldName());
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        String repeatPassword = request.getParameter(REPEAT_PASSWORD);

        User user = new User();
        user.setId(userDto.getUserId());
        user.setLogin(login);

        if (!password.equals(repeatPassword)) {
            LOGGER.log(Level.WARN, "Verify password is incorrect");
            return failure(VERIFY_PASSWORD_IS_INCORRECT, request);
        } else if(!password.isEmpty()) {
            user.setPassword(password);
            try {
                if (valid(user)) {
                    user.setPassword(DigestUtils.md5Hex(user.getPassword()));
                }
            } catch (ValidatorException e) {
                LOGGER.log(Level.WARN, e.getMessage());
                return failure(ERROR_UPDATE_USER, request);
            }
        }

        user.setPermission(userDto.getPermission());
        try {
            update(user);
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, e.getMessage());
            return failure(ERROR_UPDATE_USER, request);
        }
        request.getSession().setAttribute(DONE, USER_EDIT_SUCCESS);
        userDto = new UserDto(user);
        request.getSession().setAttribute(Constances.USER.getFieldName(), userDto);
        return new CommandResult(ResourceManager.getProperty("command.profile"), true);
    }

    private boolean valid(User user) throws ValidatorException {
        Validator validator = new UserValidator();
        String error = validator.isValid(user);
        if (error != null) {
            throw new ValidatorException(error);
        } else {
            return true;
        }
    }

    private void update(User user) throws ServiceException {
        UserService service = new UserServiceImpl();
        Integer id = service.save(user);
        if (id == null) {
            throw new ServiceException(ERROR_UPDATE_USER);
        }
    }

    private CommandResult failure(String error, HttpServletRequest request) {
        request.getSession().setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("command.profile"), true);
    }
}
