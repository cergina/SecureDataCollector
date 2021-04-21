package Model.Web;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class Measurement extends PrettyObject {
    // PARAMETERS
    @Expose
    private Integer value;
    @Expose
    private Integer sensorID;
    @Expose
    private Date measuredAt;

    // empty constructor for Gson
    public Measurement() {
    }

    public Measurement(Integer value, Integer sensorID, Date measuredAt) {
        this.value = value;
        this.sensorID = sensorID;
        this.measuredAt = measuredAt;
    }

    // GETTERS and SETTERS

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getSensorID() {
        return sensorID;
    }

    public void setSensorID(Integer sensorID) {
        this.sensorID = sensorID;
    }

    public Date getMeasuredAt() {
        return measuredAt;
    }

    public void setMeasuredAt(Date measuredAt) {
        this.measuredAt = measuredAt;
    }

}
