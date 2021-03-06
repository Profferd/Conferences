package com.hrushko.command.action.event;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.Event;
import com.hrushko.entity.UserDto;
import com.hrushko.entity.Value;
import com.hrushko.service.EventService;
import com.hrushko.service.RegistrationService;
import com.hrushko.service.RoleService;
import com.hrushko.service.ServiceException;
import com.hrushko.service.impl.EventServiceImpl;
import com.hrushko.service.impl.RegistrationServiceImpl;
import com.hrushko.service.impl.RoleServiceImpl;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import com.hrushko.util.page.EventPagination;
import com.hrushko.util.page.Pagination;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllEventsCommand implements Command {
    private static final Logger logger = LogManager.getLogger(AllEventsCommand.class);
    private static final String THERE_NOT_EVENTS = "there_not_events";
    private static final String CANT_READ_EVENTS = "cant_read_events";
    private static final Integer NOTES_PER_PAGE = 3;
    private static final String PAGE = "page";
    private static final String INVALID_PAGE = "invalid_page";
    private static final String ERROR = "error";
    private static final String SHOW_PAGES = "showPages";
    private static final String ZERO_EVENTS = "zero_events";
    private static final String ROLES = "roles";

    private Pagination pagination;


    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        List<Event> events = null;
        try {
            events = readAllEvents();
            request.setAttribute(ROLES, getRoles());
        } catch (ServiceException e) {
            logger.log(Level.INFO, e.getMessage());
            return failure(CANT_READ_EVENTS, request);
        }
        if (events.isEmpty()) {
            logger.log(Level.INFO, "there is not events yet!");
            request.setAttribute(THERE_NOT_EVENTS, true);
        }

        List<Event> userEvents = null;
        UserDto userDto = (UserDto) request.getSession().getAttribute(Constances.USER.getFieldName());
        if (userDto != null) {
            try {
                userEvents = readUserEvents(userDto.getUserId());
            } catch (ServiceException e) {
                logger.log(Level.INFO, e.getMessage());
                return failure(CANT_READ_EVENTS, request);
            }
            events.removeAll(userEvents);
        }
        Map<Integer, Integer> filling = new HashMap<>();
        for (Event event : events) {
            try {
                Integer numberOfUsers = readUsersOnEvent(event);
                filling.put(event.getId(), numberOfUsers);
            } catch (ServiceException e) {
                logger.log(Level.INFO, e.getMessage());
                return failure(CANT_READ_EVENTS, request);
            }
        }


        if (events.isEmpty()) {
            request.getSession().setAttribute(ERROR, ZERO_EVENTS);
        }
        request.setAttribute("events", getPage(events, request));
        request.setAttribute("countOfPages", pagination.getCountOfPages());
        request.setAttribute(PAGE, pagination.getPage());
        if (NOTES_PER_PAGE >= events.size()) {
            request.setAttribute(SHOW_PAGES, false);
        } else {
            request.setAttribute(SHOW_PAGES, true);
        }
        request.setAttribute("filling", filling);
        request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.eventInfo"));
        return new CommandResult(ResourceManager.getProperty("page.main"));
    }

    private List<Event> getPage(List<Event> events, HttpServletRequest request) {
        String pageString = request.getParameter(PAGE);
        pagination = new EventPagination(NOTES_PER_PAGE);
        if (pageString != null) {
            try {
                int page = Integer.parseInt(pageString);
                return pagination.getPage(events, page);
            } catch (NumberFormatException e) {
                logger.log(Level.WARN, INVALID_PAGE);
            }
        }
        Integer page1 = 1;
        return pagination.getPage(events, 1);


    }

    private Integer readUsersOnEvent(Event event) throws ServiceException {
        RegistrationService service = new RegistrationServiceImpl();
        Integer integer = service.findUsersOnEvent(event.getId()).size();
        if (integer == null) {
            throw new ServiceException();
        }
        return integer;

    }

    private CommandResult failure(String error, HttpServletRequest request) {
        request.setAttribute(ERROR, error);
        request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.eventInfo"));
        return new CommandResult(ResourceManager.getProperty("page.main"));
    }

    private List<Event> readUserEvents(Integer userId) throws ServiceException {
        RegistrationService service = new RegistrationServiceImpl();
        return service.findUserEvents(userId);
    }

    private List<Event> readAllEvents() throws ServiceException {
        EventService service = new EventServiceImpl();
        return service.findAll();
    }

    private List<Value> getRoles() throws ServiceException {
        RoleService service = new RoleServiceImpl();
        return service.findAll();
    }
}
