package Model.Web;

import Model.Web.PrettyObject;
import com.google.gson.annotations.Expose;

public class FlatOwner extends PrettyObject {
    // PARAMETERS
    @Expose
    public String title;
    @Expose
    public String firstName;
    @Expose
    public String middleName;
    @Expose
    public String lastName;
    @Expose
    public String phone;
    @Expose
    public String email;
    @Expose
    public String address;

    // empty constructor for Gson
    public FlatOwner() {}

    public FlatOwner(String title, String firstName, String middleName, String lastName, String phone, String email, String address) {
        this.title = title;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

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
