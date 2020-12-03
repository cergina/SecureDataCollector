package Database.Support;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletHelper {
    public static void Send404(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
