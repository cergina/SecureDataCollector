package View.Support.ServletAbstracts;

import Model.Database.Support.CustomLogs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class SessionServlet extends ConnectionServlet {

    @Override
    public boolean checkPrivilege(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Request has no session
        if (req.getSession(false) == null) {
            CustomLogs.Error("User trying to access site. [No session] FAILED.");
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}
