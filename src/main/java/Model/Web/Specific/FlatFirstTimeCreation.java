package Model.Web.Specific;

import Model.Web.PrettyObject;
import com.google.gson.annotations.Expose;

public class FlatFirstTimeCreation extends PrettyObject {
    @Expose
    String apartmentNo;
    // Flatowner flat
    @Expose
    FlatOwner owner1;
    @Expose
    FlatOwner owner2;

    //ControllerUnit
    @Expose
    Integer uid;
    @Expose
    String dip;
    @Expose
    String zwave;

    // Info about centralUnit
    @Expose
    int centralUnitId;

    // GETTERS and SETTERS
    public String getApartmentNo() {
        return apartmentNo;
    }

    public void setApartmentNo(String apartmentNo) {
        this.apartmentNo = apartmentNo;
    }

    public FlatOwner getOwner1() {
        return owner1;
    }

    public void setOwner1(FlatOwner owner1) {
        this.owner1 = owner1;
    }

    public FlatOwner getOwner2() {
        return owner2;
    }

    public void setOwner2(FlatOwner owner2) {
        this.owner2 = owner2;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getDip() {
        return dip;
    }

    public void setDip(String dip) {
        this.dip = dip;
    }

    public String getZwave() {
        return zwave;
    }

    public void setZwave(String zwave) {
        this.zwave = zwave;
    }

    public int getCentralUnitId() {
        return centralUnitId;
    }

    public void setCentralUnitId(int centralUnitId) {
        this.centralUnitId = centralUnitId;
    }
}
