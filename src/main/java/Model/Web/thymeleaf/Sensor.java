package Model.Web.thymeleaf;

/**
 * Thymeleaf model
 */
public class Sensor {

    public String input;
    public String name;
    public Integer measuredLast30Days;
    public Integer measuredTotal;

    public Sensor(String input, String name, Integer measuredLast30Days, Integer measuredTotal) {
        this.input = input;
        this.name = name;
        this.measuredLast30Days = measuredLast30Days;
        this.measuredTotal = measuredTotal;
    }
}
