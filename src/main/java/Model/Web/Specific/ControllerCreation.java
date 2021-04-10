package Model.Web.Specific;

import Model.Web.PrettyObject;
import com.google.gson.annotations.Expose;

public class ControllerCreation  extends PrettyObject {
    // PARAMETERS
    @Expose
    private int uid;

    @Expose
    private String dipAddress;

    @Expose
    private String zwave;

    @Expose
    private int flatId;

    //GETTERS and SETTERS

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDipAddress() {
        return dipAddress;
    }

    public void setDipAddress(String dipAddress) {
        this.dipAddress = dipAddress;
    }

    public String getZwave() {
        return zwave;
    }

    public void setZwave(String zwave) {
        this.zwave = zwave;
    }

    public int getFlatId() {
        return flatId;
    }

    public void setFlatId(int flatId) {
        this.flatId = flatId;
    }
}
