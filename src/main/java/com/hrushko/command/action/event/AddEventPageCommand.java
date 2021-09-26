package com.hrushko.command.action.event;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.Value;
import com.hrushko.service.ServiceException;
import com.hrushko.service.ThemeService;
import com.hrushko.service.impl.ThemeServiceImpl;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AddEventPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger(AddEventPageCommand.class);
    public static final String ERROR = "error";
    private static final String SOME_WENT_WRONG = "some_went_wrong";
    private static final String THEMES = "themes";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        List<Value> themes = null;
        try {
            themes = getThemes();
        } catch (ServiceException e) {
            logger.log(Level.WARN, "Can't read themes");
            failure(request, SOME_WENT_WRONG);
        }
        request.setAttribute(THEMES, themes);
        request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.addEvent"));
        String page = ResourceManager.getProperty("page.main");
        return new CommandResult(page);
    }

    private CommandResult failure(HttpServletRequest request, String error) {
        request.setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("command.home"), true);

    }

    private List<Value> getThemes() throws ServiceException {
        ThemeService service = new ThemeServiceImpl();
        return service.findAll();
    }
}
