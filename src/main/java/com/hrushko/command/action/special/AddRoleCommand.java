package com.hrushko.command.action.special;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.Value;
import com.hrushko.service.RoleService;
import com.hrushko.service.ServiceException;
import com.hrushko.service.impl.RoleServiceImpl;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddRoleCommand implements Command {
    private static final String ROLE_ADD_SUCCESS = "role.add.success";
    private static final String DONE = "done";
    private static final Logger LOGGER = LogManager.getLogger(AddRoleCommand.class);
    private static final String ROLE = "role";
    private static final String INVALID_INPUT = "invalid.input";
    private static final String ERROR = "error";
    private static final String ERROR_SAVE_ROLE = "error.save.role";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String role = request.getParameter(ROLE);
        if (role == null || role.isEmpty()) {
            LOGGER.log(Level.WARN, INVALID_INPUT);
            return failure(INVALID_INPUT, request);
        }
        try {
            saveRole(role);
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, e.getMessage());
            return failure(ERROR_SAVE_ROLE, request);
        }
        request.getSession().setAttribute(DONE, ROLE_ADD_SUCCESS);
        return new CommandResult(ResourceManager.getProperty("command.addRolePage"), true);
    }

    private void saveRole(String role) throws ServiceException {
        RoleService service = new RoleServiceImpl();
        service.save(new Value(role));
    }

    private CommandResult failure(String error, HttpServletRequest request) {
        request.getSession().setAttribute(ERROR, error);
        return new CommandResult(ResourceManager.getProperty("command.add.rolePage"), true);
    }
}
