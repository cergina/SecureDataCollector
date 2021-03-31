package Model.Web;

import com.google.gson.annotations.Expose;

import java.sql.Date;

public class Project extends PrettyObject{
    // PARAMETERS
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private Date createdat;
    @Expose
    private Date deletedat;

    // empty constructor for Gson
    public Project() {}

    // GETTERS
    public int getId() { return id; }

    public String getName() { return name; }

    public Date getCreatedat() {
        return createdat;
    }

    public Date getDeletedat() {
        return deletedat;
    }

    // SETTERS
    public void setId(int id) { this.id = id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public void setDeletedat(Date deletedat) {
        this.deletedat = deletedat;
    }

}