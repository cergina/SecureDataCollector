package com.collector.web;

import Database.Interaction.Entities.Address;
import Database.Support.CustomLogs;
import Database.Support.JSONHelper;
import Database.Tables.T_Address;
import com.collector.config.TemplateEngineUtil;
import org.json.JSONObject;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegisterServlet", urlPatterns = RegisterServlet.SERVLET_URL)
public class RegisterServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/register";
    public static final String TEMPLATE_NAME = "register.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        String email = request.getParameter("email");

        boolean emailExists = true; // TODO check email in database
        if (emailExists) {
            String password = request.getParameter("password");
            // TODO hash password
            String firstname = request.getParameter("firstname");
            // TODO save user data to database

            response.setStatus(HttpServletResponse.SC_CREATED);
            writer.println("User registered.");
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            writer.println("This email cannot be registered.");
        }
        writer.close();
    }
}
