package com.hrushko.command.action.special;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.Value;
import com.hrushko.service.RoleService;
import com.hrushko.service.ServiceException;
import com.hrushko.service.impl.RoleServiceImpl;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class AddRolePageCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(AddRolePageCommand.class);
    private static final String EXIST_ROLES = "existRoles";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        List<Value> existRoles = new ArrayList<>();
        try {
            existRoles = readExistRole();
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, "Can't readById existing roles");
        }
        request.setAttribute(EXIST_ROLES, existRoles);
        request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.addRole"));
        String page = ResourceManager.getProperty("page.main");
        return new CommandResult(page);
    }

    private List<Value> readExistRole() throws ServiceException {
        RoleService service = new RoleServiceImpl();
        return service.findAll();
    }
}
