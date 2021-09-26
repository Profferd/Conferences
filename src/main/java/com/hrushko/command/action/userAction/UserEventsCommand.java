package com.hrushko.command.action.userAction;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.Event;
import com.hrushko.entity.UserDto;
import com.hrushko.service.RegistrationService;
import com.hrushko.service.ServiceException;
import com.hrushko.service.impl.RegistrationServiceImpl;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UserEventsCommand implements Command {
    private static final Logger logger = LogManager.getLogger(UserEventsCommand.class);
    private static final String ERROR = "error";
    private static final String ZERO_EVENTS = "zero_events";
    private static final String CANT_FIND_USER = "cant_find_user";
    private static final String ERROR_FIND_USER_EVENTS = "events.notFound";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        UserDto userDto = (UserDto) request.getSession().getAttribute(Constances.USER.getFieldName());
        List<Event> events = null;
        if (userDto != null) {
            try {
                events = findUserEvents(userDto);
            } catch (ServiceException e) {
                logger.log(Level.WARN, e.getMessage());
                failure(request, e.getMessage());
            }
            if (events.isEmpty()) {
                request.getSession().setAttribute(ERROR, ZERO_EVENTS);
            }
            request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.userEvents"));
            request.getSession().setAttribute("events", events);
            return new CommandResult(ResourceManager.getProperty("page.main"));
        } else {
            logger.log(Level.WARN, CANT_FIND_USER);
            return failure(request, CANT_FIND_USER);
        }
    }

    private List<Event> findUserEvents(UserDto userDto) throws ServiceException {
        RegistrationService service = new RegistrationServiceImpl();
        List<Event> events = service.findUserEvents(userDto.getUserId());
        if (events != null) {
            return events;
        } else {
            throw new ServiceException(ERROR_FIND_USER_EVENTS);
        }
    }

    private CommandResult failure(HttpServletRequest request, String error) {
        request.getSession().setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("page.main"), true);
    }
}
