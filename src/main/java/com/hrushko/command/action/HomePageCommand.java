package com.hrushko.command.action;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomePageCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(HomePageCommand.class);
    private static final String LANGUAGE = "language";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String language = "en";
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equalsIgnoreCase(LANGUAGE)){
                    LOGGER.log(Level.INFO, "cookies has been read");
                    language = cookie.getValue();
                }
            }
        }
        request.getSession().setAttribute(LANGUAGE, language);
        String page = ResourceManager.getProperty("page.main");
        return new CommandResult(page);
    }
}
