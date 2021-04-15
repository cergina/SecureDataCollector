package Model.Web;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Users {

    // PARAMETERS
    @Expose
    private List<User> users;

    public Users() {}

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
