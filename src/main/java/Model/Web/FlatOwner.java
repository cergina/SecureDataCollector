package Model.Web;

import Model.Web.PrettyObject;
import com.google.gson.annotations.Expose;

public class FlatOwner extends PrettyObject {
    // PARAMETERS
    @Expose
    private String title;
    @Expose
    private String firstName;
    @Expose
    private String middleName;
    @Expose
    private String lastName;
    @Expose
    private String phone;
    @Expose
    private String email;
    @Expose
    private String address;

    // GETTERS and SETTERS
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
