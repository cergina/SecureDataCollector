package Model.Web;


import com.google.gson.Gson;

public class Auth extends PrettyObject {

    private User user;
    private boolean isadmin;
    private String password;
    private String verificationcode;

    // ak potrebujes dopln si iny konstruktor

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

    public static Auth parse(String str){
        return new Gson().fromJson(str, Auth.class);
    }
}
