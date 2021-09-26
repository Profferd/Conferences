package com.hrushko.filter;

import com.hrushko.entity.UserDto;
import com.hrushko.util.Constances;
import com.hrushko.util.ResourceManager;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AccessFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(AccessFilter.class);
    private static final String COMMAND = "command";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String command = request.getParameter(COMMAND);
        AccessSystem.updateRules((UserDto) request.getSession().getAttribute(Constances.USER.getFieldName()));
        try {
            if (command != null && AccessSystem.checkAccess(command)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                LOGGER.log(Level.WARN, "no access for this user");
                dispatch(servletRequest, servletResponse, ResourceManager.getProperty("page.error405"));
            }
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARN, "illegal command " + command);
            dispatch(servletRequest, servletResponse, ResourceManager.getProperty("page.error404"));
        }
    }

    @Override
    public void destroy() {
    }

    private void dispatch(ServletRequest servletRequest, ServletResponse servletResponse, String page) throws ServletException, IOException {
        RequestDispatcher dispatcher = servletRequest.getRequestDispatcher("/" + page);
        dispatcher.forward(servletRequest, servletResponse);
    }
}
