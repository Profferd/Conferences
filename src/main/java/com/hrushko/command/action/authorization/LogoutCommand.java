package com.hrushko.command.action.authorization;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.util.ResourceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.hrushko.util.Constances.*;

public class LogoutCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        HttpSession session = request.getSession();
        session.removeAttribute(PERMISSION.getFieldName());
        session.removeAttribute(USER.getFieldName());
        session.removeAttribute(MENU.getFieldName());
        return new CommandResult(ResourceManager.getProperty("command.home"), true);
    }
}
