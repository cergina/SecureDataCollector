package View.Support.ServletAbstracts;

import Model.Database.Support.CustomLogs;
import View.Support.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AdminServlet extends SessionServlet {

    @Override
    public boolean checkPrivilege(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Request has no session
        if (super.checkPrivilege(req, resp) == false) {
            return false;
        }

        // Session does not map to Admin user
        if (SessionUtil.getIsadmin(req.getSession(false)) == false) {
            CustomLogs.Error("User trying to access admin site. [Not admin] FAILED., ip: " + req.getRemoteAddr());
            return false;
        }

        return true;
    }
}
