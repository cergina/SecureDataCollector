package View.Web.Servlets.Privileged;

import Control.Scenario.UC_NewSensor;
import Model.Web.JsonResponse;
import Model.Web.PrettyObject;
import Model.Web.Sensor;
import View.Support.ServletAbstracts.AdminServlet;
import View.Support.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Admin_SensorCreateServlet", urlPatterns = Admin_SensorCreateServlet.SERVLET_URL)
public class Admin_SensorCreateServlet extends AdminServlet {
    public static final String SERVLET_URL =  "/admin/sensors/create";


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter writer = response.getWriter();

        Sensor sensor = (Sensor) PrettyObject.parse(ServletHelper.RequestBody(request), Sensor.class);
        JsonResponse jsonResponse = (new UC_NewSensor(getDb()).createSensor(sensor));
        response.setStatus(jsonResponse.getStatus());

        writer.println(jsonResponse.toString());
        writer.close();
    }
}
