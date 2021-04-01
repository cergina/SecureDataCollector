package Model.Web.thymeleaf;

/**
 * Thymeleaf model
 */
public class Address {

    public String country;
    public String city;
    public String street;
    public String houseNO;
    public String zip;

    public Address(String country, String city, String street, String houseNO, String zip) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNO = houseNO;
        this.zip = zip;
    }
}
