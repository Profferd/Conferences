package com.hrushko.command.action.special;

import com.hrushko.command.CommandException;
import com.hrushko.command.CommandResult;
import com.hrushko.command.action.Command;
import com.hrushko.entity.Permission;
import com.hrushko.service.PermissionService;
import com.hrushko.service.ServiceException;
import com.hrushko.service.impl.PermissionServiceImpl;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class AddPermissionPageCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(AddPermissionPageCommand.class);
    private static final String EXIST_PERMISSION = "existPermission";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        List<Permission> permissions = new ArrayList<>();
        try {
            permissions = readExistPermission();
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, "Can't find existing permission");
        }
        request.setAttribute(EXIST_PERMISSION, permissions);
        request.setAttribute(Constances.INCLUDE.getFieldName(), ResourceManager.getProperty("page.addPermission"));
        String page = ResourceManager.getProperty("page.main");
        return new CommandResult(page);
    }

    private List<Permission> readExistPermission() throws ServiceException {
        PermissionService service = new PermissionServiceImpl();
        return service.readAll();
    }
}
