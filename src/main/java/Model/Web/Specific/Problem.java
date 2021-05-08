package Model.Web.Specific;

import Model.Database.Interaction.ComplexInteractions.Others;
import Model.Web.*;
import com.google.gson.annotations.Expose;

import java.util.Dictionary;

public class Problem extends PrettyObject {
    @Expose
    public String reason;
    @Expose
    public Sensor sensor;
    @Expose
    public FlatOwner owner;
    @Expose
    public Address address;
    @Expose
    public Flat flat;
    @Expose
    public Project project;
    @Expose
    public String developerMail;
    @Expose
    public String developerName;

    // empty constructor for Gson
    public Problem() {
    }

    // constructor for dictionary
    public Problem(Dictionary dict, String problem) {
        this.reason = problem;

        this.sensor = new Sensor();
        this.sensor.setInput((String)dict.get(Others.COMPLEX_PROBLEM_INPUT));
        this.sensor.setName((String)dict.get(Others.COMPLEX_PROBLEM_SENSORNAME));

        this.owner = new FlatOwner();
        this.owner.setPhone((String)dict.get(Others.COMPLEX_PROBLEM_FO_PHONE));
        this.owner.setFirstName((String)dict.get(Others.COMPLEX_PROBLEM_FO_FIRSTNAME));
        this.owner.setLastName((String)dict.get(Others.COMPLEX_PROBLEM_FO_LASTNAME));

        this.address = new Address();
        this.address.setCountry((String)dict.get(Others.COMPLEX_PROBLEM_ADDR_COUNTRY));
        this.address.setCity((String)dict.get(Others.COMPLEX_PROBLEM_ADDR_CITY));
        this.address.setStreet((String)dict.get(Others.COMPLEX_PROBLEM_ADDR_STREET));
        this.address.setHouseno((String)dict.get(Others.COMPLEX_PROBLEM_ADDR_NO));

        this.flat = new Flat();
        this.flat.setApartmentNO((String)dict.get(Others.COMPLEX_PROBLEM_APARTMENT));

        this.project = new Project();
        this.project.setName((String)dict.get(Others.COMPLEX_PROBLEM_PROJ_NAME));

        this.setDeveloperMail((String)dict.get(Others.COMPLEX_PROBLEM_PROJ_OWNER_MAIL));
        this.setDeveloperName((String)dict.get(Others.COMPLEX_PROBLEM_PROJ_OWNER_NAME));
    }

    // GETTERS & SETTERS
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public FlatOwner getOwner() {
        return owner;
    }

    public void setOwner(FlatOwner owner) {
        this.owner = owner;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getDeveloperMail() {
        return developerMail;
    }

    public void setDeveloperMail(String developerMail) {
        this.developerMail = developerMail;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }
}
