package Model.Web.Specific;

import Model.Web.PrettyObject;
import com.google.gson.annotations.Expose;

public class BuildingCreation extends PrettyObject {
    @Expose
    private int addressId;
    @Expose
    private int projectId;

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
