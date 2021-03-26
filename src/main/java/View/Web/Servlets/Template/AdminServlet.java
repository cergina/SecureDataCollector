package View.Web.Servlets.Template;

import View.Support.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AdminServlet extends SessionServlet {

    @Override
    public boolean checkPrivilege(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!super.checkPrivilege(req, resp)) {
            return false;
        }
        if (!SessionUtil.getIsadmin(req.getSession(false))) { // is not admin?
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }
}
