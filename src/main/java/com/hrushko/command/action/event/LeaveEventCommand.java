package com.hrushko.command.action.event;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.Registration;
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

public class LeaveEventCommand implements Command {
    public static final String ERROR = "error";
    public static final String EVENT_LEAVE_SUCCESS = "event.leave.success";
    private static final Logger logger = LogManager.getLogger(LeaveEventCommand.class);
    private static final String EVENT_ID = "eventId";
    private static final String DONE = "done";
    private static final String THERES_NO_SUCH_REGISTRATION = "theres_no_such_registration";
    private static final String CANT_FIND_EVENT = "event.notfound";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        clearNotification(request);
        if (request.getParameter(EVENT_ID) == null) {
            logger.log(Level.WARN, "Can't find event");
            return failure(CANT_FIND_EVENT, request);
        }
        UserDto userDto = (UserDto) request.getSession().getAttribute(Constances.USER.getFieldName());
        Integer userId = userDto.getUserId();
        Integer eventId;
        try {
            eventId = Integer.valueOf(request.getParameter(EVENT_ID));
            unregister(eventId, userId);
        } catch (NumberFormatException e) {
            logger.log(Level.WARN, CANT_FIND_EVENT);
            failure(CANT_FIND_EVENT, request);
        } catch (ServiceException e) {
            logger.log(Level.WARN, e.getMessage());
            return failure(THERES_NO_SUCH_REGISTRATION, request);
        }
        request.getSession().setAttribute(DONE, EVENT_LEAVE_SUCCESS);
        return new CommandResult(ResourceManager.getProperty("command.userEvents"), true);
    }

    private void clearNotification(HttpServletRequest request) {
        request.getSession().removeAttribute(DONE);
        request.getSession().removeAttribute(ERROR);
    }

    private void unregister(Integer eventId, Integer userId) throws ServiceException {
        RegistrationService service = new RegistrationServiceImpl();
        Registration registration = service.readByUserAndEvent(eventId, userId);
        if (registration != null) {
            service.delete(registration.getId());
        } else {
            throw new ServiceException(THERES_NO_SUCH_REGISTRATION);
        }
    }

    private CommandResult failure(String error, HttpServletRequest request) {
        request.getSession().setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("command.userEvents"), true);
    }
}
