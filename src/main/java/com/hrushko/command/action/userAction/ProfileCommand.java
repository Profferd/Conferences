package com.hrushko.command.action.userAction;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.User;
import com.hrushko.entity.UserDto;
import com.hrushko.entity.UserInfo;
import com.hrushko.service.ServiceException;
import com.hrushko.service.UserInfoService;
import com.hrushko.service.UserService;
import com.hrushko.service.impl.UserInfoServiceImpl;
import com.hrushko.service.impl.UserServiceImpl;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfileCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ProfileCommand.class);
    private static final String ERROR_FIND_USER_DTO = "error_find_user_dto";
    private static final String ERROR_FIND_USER = "error_find_user";
    private static final String ERROR_FIND_USER_INFO = "error_find_user_info";
    private static final String ERROR = "error";
    private static final String DONE = "done";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        UserDto userDto = (UserDto) request.getSession().getAttribute(Constances.USER.getFieldName());
        UserInfo info = null;
        if (userDto != null) {
            try {
                User user = findUser(userDto);
                info = findUserInfo(user);
            } catch (ServiceException e) {
                LOGGER.log(Level.WARN, e.getMessage());
                failure(request, e.getMessage());
            }
            request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.profile"));
            request.getSession().setAttribute(Constances.USER_INFO.getFieldName(), info);
            return new CommandResult(ResourceManager.getProperty("page.main"));
        } else {
            LOGGER.log(Level.WARN, ERROR_FIND_USER_DTO);
            return new CommandResult(ResourceManager.getProperty("command.logout"), true);
        }
    }

    private User findUser(UserDto userDto) throws ServiceException {
        UserService service = new UserServiceImpl();
        User user = service.findById(userDto.getUserId());
        if (user != null) {
            return  user;
        } else {
            throw new ServiceException(ERROR_FIND_USER);
        }
    }

    private UserInfo findUserInfo(User user) throws ServiceException {
        UserInfoService service = new UserInfoServiceImpl();
        UserInfo info;
        info = service.findByUser(user);
        if (info != null) {
            return info;
        } else {
            throw new ServiceException(ERROR_FIND_USER_INFO);
        }
    }

    private CommandResult failure(HttpServletRequest request, String error) {
        request.getSession().setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("command.home"), true);
    }
}
