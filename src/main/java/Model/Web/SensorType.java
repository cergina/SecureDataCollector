package Model.Web;

import com.google.gson.annotations.Expose;

import java.util.List;

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
    public List<CommType> commTypes;

    // empty constructor for Gson
    public SensorType() {}

    public SensorType(Integer id, String name, String measuredin) {
        this.id = id;
        this.name = name;
        this.measuredin = measuredin;
    }

    public SensorType(Integer id, String name, String measuredin, List<CommType> commTypes) {
        this.id = id;
        this.name = name;
        this.measuredin = measuredin;
        this.commTypes = commTypes;
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

    public List<CommType> getCommTypes() {
        return commTypes;
    }

    public void setCommTypes(List<CommType> commTypes) {
        this.commTypes = commTypes;
    }
}
