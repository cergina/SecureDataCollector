package Model.Web;

import com.google.gson.annotations.Expose;

/**
 * Api model - address
 */
public class Address extends PrettyObject {

    @Expose
    public Integer id;
    @Expose
    public String country;
    @Expose
    public String city;
    @Expose
    public String street;
    @Expose
    public String houseno;
    @Expose
    public String zip;

    // empty constructor for Gson
    public Address() {
    }

    public Address(Integer id, String country, String city, String street, String houseno, String zip) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseno = houseno;
        this.zip = zip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseno() {
        return houseno;
    }

    public void setHouseno(String houseno) {
        this.houseno = houseno;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
