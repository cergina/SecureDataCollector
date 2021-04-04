package Model.Web;


import com.google.gson.annotations.Expose;

/**
 * Api model - user
 */
public class User extends PrettyObject {

    // PARAMETERS
    @Expose
    private String email;
    @Expose
    private String beforetitle;
    @Expose
    private String firstname;
    @Expose
    private String middlename;
    @Expose
    private String lastname;
    @Expose
    private String phone;
    @Expose
    private String residence;
    private Integer userID;

    // empty constructor for Gson
    public User() {}

    // GETTERS
    public String getEmail() {
        return email;
    }

    public String getBeforetitle() {
        return beforetitle;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhone() {
        return phone;
    }

    public String getResidence() {
        return residence;
    }

    public Integer getUserID() {
        return userID;
    }

    // SETTERS
    public void setEmail(String email) {
        this.email = email;
    }

    public void setBeforetitle(String beforetitle) {
        this.beforetitle = beforetitle;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
}
