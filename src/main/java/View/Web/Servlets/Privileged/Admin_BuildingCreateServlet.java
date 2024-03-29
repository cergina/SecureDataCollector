package View.Web.Servlets.Privileged;

import Control.Scenario.UC_ListBuilding;
import Model.Database.Support.CustomLogs;
import Model.Web.Building;
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

@WebServlet(name = "Admin_BuildingCreateServlet", urlPatterns = Admin_BuildingCreateServlet.SERVLET_URL)
public class Admin_BuildingCreateServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/buildings/create";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter writer = response.getWriter();

        JsonResponse jsonResponse = new JsonResponse();
        try {
            Building buildingCreation = (Building) PrettyObject.parse(ServletHelper.RequestBody(request), Building.class);

            // Execute creation and confirm to user
            jsonResponse = (new UC_ListBuilding(getDb()).createNewBuilding(buildingCreation));
            response.setStatus(jsonResponse.getStatus());
        } catch (Exception e) {
            CustomLogs.Error(e.getMessage() + "Someone is tampering with our API and sending forged requests and invalid id's.");

            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage("Invalid ID's. Tampering with API detected..");
        }

        writer.println(jsonResponse);
        writer.close();
    }
}
