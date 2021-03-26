package View.Support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PrivilegeInterface {

    /**
     * Check whether request sender is allowed to request this route
     * @return true if is allowed, false otherwise
     */
    boolean checkPrivilege(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
