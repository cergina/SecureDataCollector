package View.Support;

import Control.Connect.DbProvider;

import javax.servlet.http.HttpServlet;

public abstract class POST_Database_Interaction extends HttpServlet {
    protected DbProvider dbProvider;

    // GENERIC, has to be in every Servlet class, abstract, or extend does not work, tried
    public void init (){
        dbProvider = new DbProvider();
    }

    public void destroy() {
        dbProvider.disconnect();
    }
}
