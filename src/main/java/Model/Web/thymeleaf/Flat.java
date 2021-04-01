package Model.Web.thymeleaf;

import java.util.List;

/**
 * Thymeleaf model
 */
public class Flat {

    public String apartmentNO;
    public Address address;
    public CentralUnit centralUnit;
    public List<ControllerUnit> controllerUnits;

    public Flat(String apartmentNO, Address address, CentralUnit centralUnit, List<ControllerUnit> controllerUnits) {
        this.apartmentNO = apartmentNO;
        this.address = address;
        this.centralUnit = centralUnit;
        this.controllerUnits = controllerUnits;
    }
}
