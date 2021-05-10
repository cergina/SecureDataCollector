package View.Support.ServletAbstracts;

import View.Support.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminEditableUserViewableServlet extends SessionServlet {

    @Override
    public boolean checkPrivilege(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Request has no session
        return super.checkPrivilege(req, resp) != false;
    }

    public boolean checkIfPrivilegeIsAdmin(HttpServletRequest req) throws IOException {
        // Session does not map to Admin user
        return SessionUtil.getIsadmin(req.getSession(false)) != false;
    }
}
