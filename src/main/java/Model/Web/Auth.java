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

    // public methods

    /***
     * Minimum required fields during admin preregistration are email, whole name, number and phone
     * @return
     */
    public boolean isModelOkayForCreation() {
        if (user == null ||
                user.getEmail().equals("") ||
                user.getFirstname().equals("") ||
                user.getLastname().equals("") ||
                user.getResidence().equals("") ||
                user.getPhone().equals("") ||
                (user.getPhone().matches("^(\\+)?[0-9 ]+$") == false))
            return false;

        return true;
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
