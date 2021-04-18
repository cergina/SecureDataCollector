package Model.Web;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class Measurement extends PrettyObject {
    // PARAMETERS
    @Expose
    private int value;
    @Expose
    private int sensorID;
    @Expose
    private Date measuredAt;

    // empty constructor for Gson
    public Measurement() {
    }

    public Measurement(int value, int sensorID, Date measuredAt) {
        this.value = value;
        this.sensorID = sensorID;
        this.measuredAt = measuredAt;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSensorID() {
        return sensorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }

    public Date getMeasuredAt() {
        return measuredAt;
    }

    public void setMeasuredAt(Date measuredAt) {
        this.measuredAt = measuredAt;
    }

    // GETTERS and SETTERS


}
