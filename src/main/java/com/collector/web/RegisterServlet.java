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

        String email = request.getParameter("email");
        String firstname = request.getParameter("firstname");
        CustomLogs.Error("-------" + email);
        CustomLogs.Error("-------" + firstname);

        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        json.put("status", 201);
        writer.print(json.toString());
        writer.close();
    }
}
