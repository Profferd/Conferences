package com.hrushko.controller;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.command.factory.CommandFactory;
import com.hrushko.command.factory.CommandType;
import com.hrushko.command.factory.RequestType;
import com.hrushko.connection.ConnectionPool;
import com.hrushko.service.ServiceException;
import com.hrushko.service.impl.ServiceImpl;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/controller")
public class Controller extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(Controller.class);
    private static final String COMMAND = "command";
    private static final String ERROR = "error";

    public void init() {
        try {
            new ServiceImpl();
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, "Error");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response, RequestType.GET);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response, RequestType.POST);
    }

    public void destroy() {
        ConnectionPool.getInstance().destroy();
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, RequestType requestType) throws ServletException, IOException {
        String command = request.getParameter(COMMAND);
        CommandType commandType = CommandType.of(command);
        Command action = CommandFactory.create(commandType);

        CommandResult result;
        try {
            if (commandType.getRequestType() != requestType) {
                throw new CommandException("Invalid type of request");
            }
            result = action.execute(request, response);
        } catch (CommandException e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);
            request.setAttribute(ERROR, e.getMessage());
            result = new CommandResult(ResourceManager.getProperty("page.error404"));
        }

        String page = result.getPage();
        if (result.isRedirect()) {
            redirect(response, page);
        } else {
            forward(request, response, page);
        }
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/" + page);
        dispatcher.forward(request, response);
    }

    private void redirect(HttpServletResponse response, String page) throws IOException {
        response.sendRedirect(page);
    }
}