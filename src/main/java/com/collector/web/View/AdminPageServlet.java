package com.collector.web.View;

import com.collector.config.TemplateEngineUtil;
import com.collector.web.ConnectionServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AdminPageServlet", urlPatterns = AdminPageServlet.SERVLET_URL)
public class AdminPageServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/admin/view";
    public static final String TEMPLATE_NAME = "admin-view.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());

        context.setVariable("zeroIfRunningLocal", zeroIfRunningLocal);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        context.setVariable("loggedUser", "Maroš Čergeť");
        context.setVariable("privileges", "admin");

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }
}
