package Model.Web;


import com.google.gson.annotations.Expose;

/**
 * Api model - authentication
 */
public class Auth extends PrettyObject {

    // PARAMETERS
    @Expose
    private User user;
    @Expose(serialize = false)
    private Boolean isadmin;
    @Expose(serialize = false)
    private String password; // password OR password hash
    @Expose
    private String verificationcode;

    // empty constructor for Gson
    public Auth() {}

    // GETTERS
    public User getUser() {
        return user;
    }

    public Boolean isIsadmin() {
        return isadmin;
    }

    public String getPassword() {
        return password;
    }

    public String getVerificationcode() {
        return verificationcode;
    }

    // SETTERS
    public void setUser(User user) {
        this.user = user;
    }

    public void setIsadmin(Boolean isadmin) {
        this.isadmin = isadmin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVerificationcode(String verificationcode) {
        this.verificationcode = verificationcode;
    }
}
