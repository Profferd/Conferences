package com.hrushko.command.action.special;

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
import java.util.ArrayList;
import java.util.List;

public class AddThemePageCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(AddThemePageCommand.class);
    private static final String EXIST_THEMES = "existThemes";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        List<Value> existThemes = new ArrayList<>();
        try {
            existThemes = readExistThemes();
        } catch (ServiceException e){
            LOGGER.log(Level.WARN, "Cant readByID existing themes");
        }
        request.setAttribute(EXIST_THEMES, existThemes);
        request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.addTheme"));
        String page = ResourceManager.getProperty("page.main");
        return new CommandResult(page);
    }

    private List<Value> readExistThemes() throws ServiceException {
        ThemeService service = new ThemeServiceImpl();
        return service.findAll();
    }
}
