package Model.Web;


import com.google.gson.annotations.Expose;

/**
 * Api model - authentication
 */
public class Auth extends PrettyObject {

    @Expose
    private User user;
    @Expose(serialize = false)
    private boolean isadmin;
    @Expose(serialize = false)
    private String password;
    @Expose
    private String verificationcode;

    public Auth() {} // empty constructor for Gson

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isIsadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationcode() {
        return verificationcode;
    }

    public void setVerificationcode(String verificationcode) {
        this.verificationcode = verificationcode;
    }
}
