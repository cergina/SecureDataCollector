package Model.Web.thymeleaf;

import Model.Web.Address;
import Model.Web.Measurement;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Thymeleaf model
 */
public class Flat {

    public Integer id;
    @Expose
    public String apartmentNO;
    public Address address;
    public List<ControllerUnit> controllerUnits;

    public Integer measuredLast30Days;
    public Integer measuredTotal;

    @Expose
    private List<Measurement> measurements;
    @Expose
    private List<Integer> dataArray;

    // temp
    public Integer buildingId;

    public Flat(Integer id, String apartmentNO, Address address, List<ControllerUnit> controllerUnits) {
        this.id = id;
        this.apartmentNO = apartmentNO;
        this.address = address;
        this.controllerUnits = controllerUnits;
    }

    public Flat(Integer id, String apartmentNO, Integer buildingId) {
        this.id = id;
        this.apartmentNO = apartmentNO;
        this.buildingId = buildingId;
    }

    public Flat(Integer id, String apartmentNO, List<Integer> dataArray){
        this.id = id;
        this.apartmentNO = apartmentNO;
        this.dataArray = dataArray;
    }

    public Flat(Integer id, String apartmentNO, Integer buildingId, Integer measuredLast30Days, Integer measuredTotal, List<Measurement> measurements, List<Integer> dataArray){
        this.id = id;
        this.apartmentNO = apartmentNO;
        this.buildingId = buildingId;
        this.measuredLast30Days = measuredLast30Days;
        this.measuredTotal = measuredTotal;
        this.measurements = measurements;
        this.dataArray = dataArray;
    }
}
