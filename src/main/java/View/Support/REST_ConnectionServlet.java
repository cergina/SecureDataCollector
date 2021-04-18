package View.Support;

import Control.Connect.DbProvider;

import javax.servlet.http.HttpServlet;
import java.io.IOException;

public abstract class REST_ConnectionServlet extends HttpServlet {
    private DbProvider dbProvider;

    // GENERIC, has to be in every Servlet class, abstract, or extend does not work, tried
    public void init (){
        dbProvider = new DbProvider();
    }

    public void destroy() {
        dbProvider.disconnect();
    }

    protected DbProvider getDb() throws IOException {
        // this has to be thrown, because if it is not we will continue execution what we dont want
        boolean isValid = dbProvider.testConnection();

        if (isValid == false) {
            dbProvider.disconnect();
            dbProvider = new DbProvider();
        }

        return dbProvider;
    }
}
