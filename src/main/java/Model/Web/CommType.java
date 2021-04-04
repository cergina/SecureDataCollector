package Model.Web;

import com.google.gson.annotations.Expose;

/**
 * Api model - communication type
 */
public class CommType extends PrettyObject {

    @Expose
    private Integer id;
    @Expose
    private String name;

    // empty constructor for Gson
    public CommType() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
