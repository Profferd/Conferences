package com.hrushko.command.action.event;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.command.factory.CommandType;
import com.hrushko.entity.Event;
import com.hrushko.entity.UserDto;
import com.hrushko.entity.Value;
import com.hrushko.filter.AccessSystem;
import com.hrushko.service.EventService;
import com.hrushko.service.ServiceException;
import com.hrushko.service.ThemeService;
import com.hrushko.service.impl.EventServiceImpl;
import com.hrushko.service.impl.ThemeServiceImpl;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class EditEventPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger(EditEventPageCommand.class);
    private static final String EVENT_ID = "eventId";
    private static final String ERROR_FIND_EVENT = "error_find_event";
    private static final String EVENT = "event";
    private static final String ERROR = "error";
    private static final String THEMES = "themes";
    private Event event;

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String eventIdString = request.getParameter(EVENT_ID);
        if (eventIdString == null) {
            logger.log(Level.WARN, ERROR_FIND_EVENT);
            return failure(ERROR_FIND_EVENT, request);
        }
        try {
            UserDto userDto = (UserDto) request.getSession().getAttribute(Constances.USER.getFieldName());
            Integer eventId = Integer.valueOf(request.getParameter(EVENT_ID));
            if (!checkRules(eventId, userDto)) {
                throw new ServiceException("No access");
            }
            List<Value> themes = getTheme();
            request.setAttribute(THEMES, themes);

            request.setAttribute(EVENT, event);
            request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.editEvent"));
            String page = ResourceManager.getProperty("page.main");
            return new CommandResult(page);
        } catch (NumberFormatException e) {
            logger.log(Level.WARN, ERROR_FIND_EVENT);
            return failure(ERROR_FIND_EVENT, request);
        } catch (ServiceException e) {
            logger.log(Level.WARN, e.getMessage());
            return failure(e.getMessage(), request);
        }
    }

    private boolean checkRules(Integer eventId, UserDto userDto) throws ServiceException {
        EventService service = new EventServiceImpl();
        event = service.findById(eventId);
        if (userDto == null) return false;
        if (userDto.getUserId().equals(event.getAuthor_id())) return true;
        return AccessSystem.checkAccess(CommandType.DELETE_ANY_EVENT);
    }

    private List<Value> getTheme() throws ServiceException {
        ThemeService service = new ThemeServiceImpl();
        return service.findAll();
    }

    private CommandResult failure(String error, HttpServletRequest request) {
        request.setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("page.error405"));
    }
}
