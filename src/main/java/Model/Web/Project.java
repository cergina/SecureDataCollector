package Model.Web;

import com.google.gson.annotations.Expose;

import java.sql.Date;

public class Project extends PrettyObject{
    // PARAMETERS
    @Expose
    public Integer id;
    @Expose
    public String name;
    @Expose
    public Date createdat;
    @Expose
    public Date deletedat;

    // empty constructor for Gson
    public Project() {}

    public Project(Integer id, String name, Date createdat, Date deletedat) {
        this.id = id;
        this.name = name;
        this.createdat = createdat;
        this.deletedat = deletedat;
    }

    // GETTERS
    public Integer getId() { return id; }

    public String getName() { return name; }

    public Date getCreatedat() {
        return createdat;
    }

    public Date getDeletedat() {
        return deletedat;
    }

    // SETTERS
    public void setId(Integer id) { this.id = id; }

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
