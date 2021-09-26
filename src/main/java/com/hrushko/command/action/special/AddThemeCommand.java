package com.hrushko.command.action.special;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.Value;
import com.hrushko.service.ServiceException;
import com.hrushko.service.ThemeService;
import com.hrushko.service.impl.ThemeServiceImpl;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddThemeCommand implements Command {
    public static final String THEME = "theme";
    private static final Logger logger = LogManager.getLogger(AddThemeCommand.class);
    private static final String ROLE = "role";
    private static final String INVALID_INPUT = "invalid.input";
    private static final String ERROR = "error";
    private static final String ERROR_SAVE_THEME = "error.save.theme";
    public static final String DONE = "done";
    public static final String THEME_ADD_SUCCESS = "theme.add.success";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String theme = request.getParameter(THEME);
        if (theme == null || theme.isEmpty()) {
            logger.log(Level.WARN, INVALID_INPUT);
            return failure(request, INVALID_INPUT);
        }
        try {
            saveTheme(theme);
        } catch (ServiceException e) {
            logger.log(Level.WARN, e.getMessage());
            return failure(request, ERROR_SAVE_THEME);
        }
        request.getSession().setAttribute(DONE, THEME_ADD_SUCCESS);
        return new CommandResult(ResourceManager.getProperty("command.addThemePage"), true);
    }

    private void saveTheme(String theme) throws ServiceException {
        ThemeService service = new ThemeServiceImpl();
        service.save(new Value(theme));
    }

    private CommandResult failure(HttpServletRequest request, String error) {
        request.getSession().setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("command.addThemePage"), true);

    }

}
