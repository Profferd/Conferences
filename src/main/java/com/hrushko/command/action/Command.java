package com.hrushko.command.action;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface Command {
    /**
     * Execute command result.
     *
     * @param request  the request
     * @param response the response
     * @return the command result
     * @throws CommandException the command exception
     */
    CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException;
}
