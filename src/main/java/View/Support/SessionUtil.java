package View.Support;

import Model.Web.User;

import javax.servlet.http.HttpSession;

public class SessionUtil {

    private static final String SESSION_ATTR_IS_ADMIN = "session_attr_is_admin";
    private static final String SESSION_ATTR_USER = "session_attr_user";


    public static void setIsadmin(HttpSession session, boolean isadmin) {
        session.setAttribute(SESSION_ATTR_IS_ADMIN, isadmin);
    }

    public static boolean getIsadmin(HttpSession session) {
        if (session.getAttribute(SESSION_ATTR_IS_ADMIN) != null) {
            return (boolean) session.getAttribute(SESSION_ATTR_IS_ADMIN);
        }
        return false;
    }

    public static void setUser(HttpSession session, User user) {
        session.setAttribute(SESSION_ATTR_USER, user);
    }

    public static User getUser(HttpSession session) {
        if (session.getAttribute(SESSION_ATTR_USER) != null) {
            return (User) session.getAttribute(SESSION_ATTR_USER);
        }
        return null;
    }
}
