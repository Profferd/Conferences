package com.hrushko.command.action;

import com.hrushko.command.CommandResult;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class ChangeLanguageCommand implements Command {
    private static final Logger logger = LogManager.getLogger(ChangeLanguageCommand.class);

    private static final String LANGUAGE = "language";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String referer = request.getHeader("referer");
        String backURI = null;
        backURI = referer.substring(referer.lastIndexOf('/') + 1); // <--Error
        if (backURI.isEmpty()) {
            backURI = ResourceManager.getProperty("command.home");
        }
        String language = request.getParameter(LANGUAGE);
        if (language == null) {
            logger.log(Level.WARN, "Parameter language is null !");
        } else {
            language = language.toUpperCase(Locale.ROOT);
            request.getSession().setAttribute("language", language);
            response.addCookie(new Cookie("language", language));
        }
        return new CommandResult(backURI, true);
    }
}
