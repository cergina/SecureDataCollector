package Model.Web;


import com.google.gson.annotations.Expose;
import com.mysql.cj.util.StringUtils;

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
    @Expose(serialize = false)
    private String oldPassword;
    @Expose
    private String verificationcode;

    // empty constructor for Gson
    public Auth() {}

    // public methods

    /***
     * Minimum required fields during admin preregistration are email, whole name, number and phone
     * @return
     */
    public boolean isModelOkayForCreation() {
        if (user == null)
            return false;

        if (StringUtils.isNullOrEmpty(user.getEmail()) ||
                StringUtils.isNullOrEmpty(user.getFirstname()) ||
                StringUtils.isNullOrEmpty(user.getLastname()) ||
                StringUtils.isNullOrEmpty(user.getResidence()) ||
                StringUtils.isNullOrEmpty(user.getPhone()))
            return false;

        return user.getPhone().matches("^(\\+)?[0-9 ]+$");
    }

    /***
     * regex validation for password creation phase
     * at least one number, at least one lowercase letter,
     * at least one uppercase letter, at least one special character,
     * no whitespaces allowed
     * betwewen 5 and 15 letters
     * @return
     */
    public boolean isSuchPasswordOkay() {
        if (password == null)
            return false;

        if (password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#.?!@$%^&*-]).{8,15}$") == false)
            return false;

        return true;
    }

    // GETTERS
    public User getUser() {
        return user;
    }

    public Boolean getIsadmin() {
        return isadmin;
    }

    public String getPassword() {
        return password;
    }

    public String getOldPassword() {
        return oldPassword;
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

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setVerificationcode(String verificationcode) {
        this.verificationcode = verificationcode;
    }
}
