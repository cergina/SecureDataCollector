package Model.Web.Specific;

import Model.Web.PrettyObject;
import com.google.gson.annotations.Expose;

public class AddressCreation extends PrettyObject {

    // PARAMETERS
    @Expose
    private String street;

    @Expose
    private String houseno;

    @Expose
    private String city;

    @Expose
    private String zip;

    @Expose
    private String country;


    // GETTERS and SETTERS
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNo() {
        return houseno;
    }

    public void setHouseNo(String houseno) {
        this.houseno = houseno;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getZIP() {
        return zip;
    }

    public void setZIP(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
