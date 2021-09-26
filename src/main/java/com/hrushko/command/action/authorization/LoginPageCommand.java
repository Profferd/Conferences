package com.hrushko.command.action.authorization;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.util.ResourceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page = ResourceManager.getProperty("page.login");
        return new CommandResult(page);
    }
}
