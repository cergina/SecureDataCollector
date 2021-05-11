package View.Support.ServletAbstracts;

import Control.ConfigClass;
import Control.Connect.DbProvider;
import View.Support.PrivilegeInterface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class ConnectionServlet extends HttpServlet implements PrivilegeInterface {

    protected static final boolean trueIfRunningRemotely = ConfigClass.RUNNING_ON_SERVER;
    protected static final String VARIABLE_LOGGED_USER = "logged_user";
    protected static final String VARIABLE_ISADMIN = "isAdmin";

    private DbProvider dbProvider;

    // GENERIC, has to be in every Servlet class, abstract, or extend does not work, tried
    public void init (){
        dbProvider = new DbProvider();
    }

    public void destroy() {
        dbProvider.disconnect();
    }

    protected DbProvider getDb() throws IOException{
        // this has to be thrown, because if it is not we will continue execution what we dont want
        boolean isValid = dbProvider.testConnection();

        if (isValid == false) {
            dbProvider.disconnect();
            dbProvider = new DbProvider();
        }

        return dbProvider;
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
