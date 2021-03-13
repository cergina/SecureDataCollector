package com.collector.web;

import Database.Support.ServletHelper;
import Database.Tables.T_Address;
import com.collector.config.TemplateEngineUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "IndexServlet", urlPatterns = IndexServlet.SERVLET_URL)
public class IndexServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/index";
    public static final String TEMPLATE_NAME = "index.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());

        context.setVariable("zeroIfRunningLocal", zeroIfRunningLocal);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        ArrayList<T_Address> arr = retrieveAllAddress();
        if (arr == null) {
            ServletHelper.Send404(response);
        } else {
            context.setVariable(T_Address.DBNAME_COUNTRY, "" + arr.get(0).getA_Country());
            context.setVariable(T_Address.DBNAME_CITY, "" + arr.get(0).getA_City());
            context.setVariable(T_Address.DBNAME_STREET, "" + arr.get(0).getA_Street());
            context.setVariable(T_Address.DBNAME_HOUSENO, "" + arr.get(0).getA_HouseNO());
            context.setVariable(T_Address.DBNAME_ZIP, "" + arr.get(0).getA_ZIP());

            engine.process(TEMPLATE_NAME, context, response.getWriter());
        }
    }
}
