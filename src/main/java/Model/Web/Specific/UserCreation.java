package Model.Web.Specific;

import Model.Database.Interaction.ComplexInteractions.Others;
import Model.Web.*;
import com.google.gson.annotations.Expose;

import java.sql.Date;
import java.util.Dictionary;

public class UserCreation extends PrettyObject {
    @Expose
    public String userName;
    @Expose
    public String privilege;
    @Expose
    public String adminName;
    @Expose
    public Date createdAt;

    // empty constructor for Gson
    public UserCreation() {
    }

    // constructor for dictionary
    public UserCreation(String userName, String privilege, String adminName, Date createdAt) {
        this.userName = userName;
        this.privilege = privilege;
        this.adminName = adminName;
        this.createdAt = createdAt;
    }

    // GETTERS & SETTERS
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrivilege() {
        return privilege;
    }
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getAdminName() {
        return adminName;
    }
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
