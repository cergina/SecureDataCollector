package com.collector.support;

import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DcsWebContext {
    public static WebContext WebContextInitForDCS(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, String runningRemotelyVariableName, boolean runningRemotely) throws IOException {
        WebContext context = new WebContext(request, response, servletContext);

        context.setVariable(runningRemotelyVariableName, runningRemotely);

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        return context;
    }
}
