package com.hrushko.command.action.event;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.command.factory.CommandType;
import com.hrushko.entity.Event;
import com.hrushko.entity.UserDto;
import com.hrushko.filter.AccessSystem;
import com.hrushko.service.EventService;
import com.hrushko.service.ServiceException;
import com.hrushko.service.impl.EventServiceImpl;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveEventCommand implements Command {
    private static final String ERROR = "error";
    private static final String DONE = "done";
    private static final Logger logger = LogManager.getLogger(RemoveEventCommand.class);
    private static final String EVENT_ID = "eventId";
    private static final String ERROR_DONT_FIND_EVENT = "error_dont_find_event";
    private static final String ERROR_REMOVING_EVENT = "event.delete.fail";
    private static final String DELETE_SUCCESS = "event.delete.success";
    private static final String ACCESS_CLOSED = "access_closed";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        if (request.getParameter(EVENT_ID) == null) {
            logger.log(Level.WARN, ERROR_DONT_FIND_EVENT);
            return failure(ERROR_DONT_FIND_EVENT, request);
        }
        String eventIdString = request.getParameter(EVENT_ID);
        try {
            Integer eventId = Integer.valueOf(eventIdString);
            if (checkRules(eventId, (UserDto) request.getSession().getAttribute(Constances.USER.getFieldName()))) {
                deleteEvent(eventId);
            } else {
                throw new ServiceException(ACCESS_CLOSED);
            }
        } catch (ServiceException | NumberFormatException e) {
            logger.log(Level.WARN, e.getMessage());
            return failure(ERROR_REMOVING_EVENT, request);
        }
        request.getSession().setAttribute(DONE, DELETE_SUCCESS);
        return new CommandResult(ResourceManager.getProperty("command.allEvents"), true);
    }

    private boolean checkRules(Integer eventId, UserDto user) throws ServiceException {
        EventService service = new EventServiceImpl();
        Event event = service.findById(eventId);
        if (user == null) return false;
        if (user.getUserId().equals(event.getAuthor_id())) return true;
        return AccessSystem.checkAccess(CommandType.DELETE_ANY_EVENT);
    }

    private void deleteEvent(Integer eventId) throws ServiceException {
        EventService service = new EventServiceImpl();
        service.delete(eventId);
    }

    private CommandResult failure(String error, HttpServletRequest request) {
        request.getSession().setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("page.error405"), true);
    }
}
