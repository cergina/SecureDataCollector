package View.Web.Servlets.Privileged;

import Control.Scenario.UC_Controller;
import Model.Web.JsonResponse;
import Model.Web.PrettyObject;
import Model.Web.Specific.ControllerCreation;
import View.Support.ServletAbstracts.AdminServlet;
import View.Support.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Admin_ControllerCreateServlet", urlPatterns = Admin_ControllerCreateServlet.SERVLET_URL)
public class Admin_ControllerCreateServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/controllers/create";


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter writer = response.getWriter();

        ControllerCreation controllerCreation = (ControllerCreation) PrettyObject.parse(ServletHelper.RequestBody(request), ControllerCreation.class);

        final JsonResponse jsonResponse = (new UC_Controller(getDb()).createControllerUnit(controllerCreation));
        response.setStatus(jsonResponse.getStatus());

        writer.println(jsonResponse.toString());
        writer.close();
    }
}
