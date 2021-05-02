package Model.Web;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Api model - flat
 */
public class Flat extends PrettyObject {

    @Expose
    public Integer id;
    @Expose
    public String apartmentNO;
    @Expose
    public List<ControllerUnit> controllerUnits;
    @Expose
    public Integer buildingId;

    @Expose
    private List<Measurement> measurements;
    @Expose
    private List<Integer> dataArray;

    // empty constructor for Gson
    public Flat() {}

    public Flat(Integer id, String apartmentNO) {
        this.id = id;
        this.apartmentNO = apartmentNO;
    }

    public Flat(Integer id, String apartmentNO, List<ControllerUnit> controllerUnits) {
        this.id = id;
        this.apartmentNO = apartmentNO;
        this.controllerUnits = controllerUnits;
    }

    public Flat(Integer id, String apartmentNO, int buildingId) {
        this.id = id;
        this.apartmentNO = apartmentNO;
        this.buildingId = buildingId;
    }

    public Flat(Integer id, String apartmentNO, int buildingId, List<Integer> dataArray){
        this.id = id;
        this.apartmentNO = apartmentNO;
        this.buildingId = buildingId;
        this.dataArray = dataArray;
    }

    // required for new flat addition
    public String getApartmentNO() {
        return apartmentNO;
    }

    public void setApartmentNO(String apartmentNO) {
        this.apartmentNO = apartmentNO;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    // irrelevant
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ControllerUnit> getControllerUnits() {
        return controllerUnits;
    }

    public void setControllerUnits(List<ControllerUnit> controllerUnits) {
        this.controllerUnits = controllerUnits;
    }
}
