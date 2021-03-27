package View.Support.ServletAbstracts;

import Control.ConfigClass;
import Control.Connect.DbProvider;
import View.Configuration.ContextUtil;
import View.Support.PrivilegeInterface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class ConnectionServlet extends HttpServlet implements PrivilegeInterface {

    protected static final boolean trueIfRunningRemotely = ConfigClass.RUNNING_ON_SERVER;

    // GENERIC, has to be in every Servlet class, abstract, or extend does not work, tried
    public void init (){
    }

    public void destroy() {
    }

    protected DbProvider getDb() {
        return ContextUtil.getDbProvider(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
    }
}
