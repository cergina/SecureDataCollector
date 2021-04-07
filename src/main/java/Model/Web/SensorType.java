package Model.Web;

import com.google.gson.annotations.Expose;

/**
 * Api model - sensor type
 */
public class SensorType extends PrettyObject {

    private Integer id;
    @Expose
    public String name;
    @Expose
    public String measuredin;
    @Expose
    public CommType commType;

    // empty constructor for Gson
    public SensorType() {}

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

    public String getMeasuredin() {
        return measuredin;
    }

    public void setMeasuredin(String measuredin) {
        this.measuredin = measuredin;
    }

    public CommType getCommType() {
        return commType;
    }

    public void setCommType(CommType commType) {
        this.commType = commType;
    }
}
