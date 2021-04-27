package View.Web.Servlets.Privileged;

import Control.Scenario.UC_NewController;
import Model.Database.Support.CustomLogs;
import Model.Web.ControllerUnit;
import Model.Web.JsonResponse;
import Model.Web.PrettyObject;
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


        JsonResponse jsonResponse = new JsonResponse();

        try {
            // exception during parsing occured when UID wasnt a number
            ControllerUnit controllerUnit = (ControllerUnit) PrettyObject.parse(ServletHelper.RequestBody(request), ControllerUnit.class);

            // Execute creation
            jsonResponse = (new UC_NewController(getDb()).createControllerUnit(controllerUnit));

            // Confirm to user
            response.setStatus(jsonResponse.getStatus());
        } catch (Exception e) {
            CustomLogs.Error(e.getMessage());

            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage("UID can be only a number.");
        } finally {
            writer.println(jsonResponse.toString());
            writer.close();
        }
    }
}
