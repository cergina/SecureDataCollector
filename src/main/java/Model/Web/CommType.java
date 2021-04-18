package Model.Web;

import com.google.gson.annotations.Expose;

/**
 * Api model - communication type
 */
public class CommType extends PrettyObject {

    private Integer id;
    @Expose
    public String name;

    // empty constructor for Gson
    public CommType() {}

    public CommType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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
