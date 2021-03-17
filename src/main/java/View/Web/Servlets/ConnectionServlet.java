package View.Web.Servlets;

import Control.ConfigClass;
import View.Support.REST_ConnectionServlet;

public abstract class ConnectionServlet extends REST_ConnectionServlet {
    protected static final boolean trueIfRunningRemotely = ConfigClass.RUNNING_ON_SERVER;
}
