package com.collector.web.servlets;

import Control.ConfigClass;
import com.collector.web.config.TemplateEngineUtil;
import com.collector.web.support.DcsWebContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = LoginServlet.SERVLET_URL)
public class LoginServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/login";
    public static final String TEMPLATE_NAME = "login.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response, request.getServletContext(),
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        boolean userExists = true; // TODO check user in database // (new UseCase(dbProvider)).retrieveUserByEmail(email);
        boolean passwordMatches = true; // TODO compare password
        if (userExists && passwordMatches) {
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            // TODO add other attributes from user object to session

            response.setStatus(HttpServletResponse.SC_OK);
            writer.println("Login successful.");

//             // in case custom token is used
//            JSONObject json = new JSONObject();
//            json.put("token", session.getId());
//            writer.println(json.toString());
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writer.println("Login failed.");
        }
        writer.close();
    }
}
