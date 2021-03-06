package com.hrushko.command.action.event;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.User;
import com.hrushko.entity.UserInfo;
import com.hrushko.service.RegistrationService;
import com.hrushko.service.ServiceException;
import com.hrushko.service.UserInfoService;
import com.hrushko.service.impl.RegistrationServiceImpl;
import com.hrushko.service.impl.UserInfoServiceImpl;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersOnEventCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(UsersOnEventCommand.class);

    private static final String EVENT_ID = "eventId";
    private static final String USERS = "users";
    private static final String USERS_INFO = "users_info";
    private static final String ERROR_FIND_USERS = "error_find_users";
    private static final String ERROR_FIND_USERS_INFO = "error_find_users_info";
    private static final String ERROR = "error";
    private static final String ERROR_FIND_EVENT = "error_find_event";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String eventIdString = request.getParameter(EVENT_ID);
        Integer eventId;
        try {
            eventId = Integer.valueOf(eventIdString);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARN, ERROR_FIND_EVENT);
            return failure(ERROR_FIND_EVENT, request);
        }
        List<User> users = null;
        try {
            users = giveUsers(eventId);
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, ERROR_FIND_USERS);
            return failure(ERROR_FIND_USERS, request);
        }
        Map<Integer, UserInfo> usersInfo = null;
        try {
            usersInfo = giveUsersInfo(users);
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, ERROR_FIND_USERS_INFO);
        }
        request.getSession().setAttribute(USERS, users);
        request.getSession().setAttribute(USERS_INFO, usersInfo);
        request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.eventUsers"));

        return new CommandResult(ResourceManager.getProperty("page.main"));
    }

    private Map<Integer, UserInfo> giveUsersInfo(List<User> users) throws ServiceException {
        Map<Integer, UserInfo> usersInfo = new HashMap<>();
        UserInfoService service = new UserInfoServiceImpl();
        for (User user : users) {
            usersInfo.put(user.getId(), service.findByUser(user));
        }
        return usersInfo;
    }

    private List<User> giveUsers(Integer eventId) throws ServiceException {
        RegistrationService service = new RegistrationServiceImpl();
        return service.findUsersOnEvent(eventId);
    }

    private CommandResult failure(String error, HttpServletRequest request) {
        request.setAttribute(ERROR, error);
        request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.allUsers"));
        return new CommandResult(ResourceManager.getProperty("page.main"));
    }
}
