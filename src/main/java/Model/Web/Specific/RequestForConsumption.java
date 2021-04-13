package Model.Web.Specific;

import Model.Web.PrettyObject;
import com.google.gson.annotations.Expose;

public class RequestForConsumption extends PrettyObject {
    // PARAMETERS
    @Expose
    private int uid;

    // GETTERS AND SETTERS
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
