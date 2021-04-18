package Model.Web;

import Model.Web.thymeleaf.Flat;
import com.google.gson.annotations.Expose;

import java.util.List;

public class CentralUnit extends PrettyObject {

    // PARAMETERS
    @Expose
    private int id;
    @Expose
    private int uid;
    @Expose
    private String dip;
    @Expose
    private String friendlyName;
    @Expose
    private String simNo;
    @Expose
    private String imei;
    @Expose
    private String zwave;
    @Expose
    private int projectId;
    @Expose
    private int addressId;

    @Expose
    public List<Flat> flats;

    // empty constructor for Gson
    public CentralUnit() {}


    // GETTERS and SETTERS
    public int getId() {
        return id;
    }

    public int getUid() {
        return uid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDip() {
        return dip;
    }

    public void setDip(String dip) {
        this.dip = dip;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getSimNo() {
        return simNo;
    }

    public void setSimNo(String simNo) {
        this.simNo = simNo;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getZwave() {
        return zwave;
    }

    public void setZwave(String zwave) {
        this.zwave = zwave;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public List<Flat> getFlats() {
        return flats;
    }

    public void setFlats(List<Flat> flats) {
        this.flats = flats;
    }
}
