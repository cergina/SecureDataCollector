package View.Web.Servlets.Privileged;

import Control.Scenario.UC_CreateFlat;
import Model.Database.Support.CustomLogs;
import Model.Web.JsonResponse;
import Model.Web.PrettyObject;
import Model.Web.Specific.Flat_FlatOwners_Creation;
import View.Support.ServletAbstracts.AdminServlet;
import View.Support.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Admin_FlatForBuildingCreateServlet", urlPatterns = Admin_FlatForBuildingCreateServlet.SERVLET_URL)
public class Admin_FlatForBuildingCreateServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/flat/create";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter writer = response.getWriter();

        JsonResponse jsonResponse = new JsonResponse();

        // parse JSON from Body object
        try {
            // exception during parsing occured when some fields werent a number
            Flat_FlatOwners_Creation flatFlatOwnersCreation = (Flat_FlatOwners_Creation) PrettyObject.parse(ServletHelper.RequestBody(request), Flat_FlatOwners_Creation.class);

            // Execute creation
            jsonResponse = (new UC_CreateFlat(getDb()).createNewFlat_FlatOwner(flatFlatOwnersCreation)); // create new flat, owner(s)

            // Confirm to user
            response.setStatus(jsonResponse.getStatus());
        } catch (Exception e) {
            CustomLogs.Error(e.getMessage());

            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage("Make sure you are inserting fields in ok format.");
        } finally {
            writer.println(jsonResponse);
            writer.close();
        }
    }
}
