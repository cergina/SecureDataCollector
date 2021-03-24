package View.Web.Servlets;

import Control.ConfigClass;
import Control.Connect.DbProvider;
import View.Configuration.ContextUtil;
import View.Support.REST_ConnectionServlet;

import javax.servlet.http.HttpServlet;

public abstract class ConnectionServlet extends HttpServlet {

    protected static final boolean trueIfRunningRemotely = ConfigClass.RUNNING_ON_SERVER;

    // GENERIC, has to be in every Servlet class, abstract, or extend does not work, tried
    public void init (){
    }

    public void destroy() {
    }

    protected DbProvider getDb() {
        return ContextUtil.getDbProvider(getServletContext());
    }
}
