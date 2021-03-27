package View.Support.ServletAbstracts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class PublicServlet extends ConnectionServlet {

    @Override
    public boolean checkPrivilege(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        return true;
    }
}
