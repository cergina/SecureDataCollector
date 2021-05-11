package View.Web.Servlets.Public;

import View.Support.ServletAbstracts.PublicServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = LogoutServlet.SERVLET_URL)
public class LogoutServlet extends PublicServlet {
    public static final String SERVLET_URL =  "/logout";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);
        if (checkPrivilege(request, response) == false) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        HttpSession session = request.getSession();
        if (session != null) session.invalidate();

        response.sendRedirect("/dcs");
    }
}
