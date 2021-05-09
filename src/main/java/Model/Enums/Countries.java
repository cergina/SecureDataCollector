package Model.Enums;

import java.util.ArrayList;
import java.util.List;

public enum Countries {
    SLOVAKIA("Slovakia"),
    CZECH_REPUBLIC("Czech Republic"),
    AUSTRIA("Austria"),
    HUNGARY("Hungary"),
    POLAND("Poland"),
    UKRAINE("Ukraine"),
    ALBANIA("Albania"),
    ANDORRA("Andorra"),
    ARMENIA("Armenia"),
    AZERBAIJAN("Azerbaijan"),
    BELARUS("Belarus"),
    BELGIUM("Belgium"),
    BOSNIA_AND_HERCEGOVINA("Bosnia and Hercegovina"),
    BULGARIA("Bulgaria"),
    CROATIA("Croatia"),
    CYPRUS("Cyprus"),
    DENMARK("Denmark"),
    ESTONIA("Estonia"),
    FINLAND("Finland"),
    FRANCE("France"),
    GEORGIA("Georgia"),
    GERMANY("Germany"),
    GREECE("Greece"),
    ICELAND("Iceland"),
    IRELAND("Ireland"),
    ITALY("Italy"),
    KAZAKHSTAN("Kazakhstan"),
    KOSOVO("Kosovo"),
    LATVIA("Latvia"),
    LIECHTENSTEIN("Liechtenstein"),
    LITHUANIA("Lithuania"),
    LUXEMBOURG("Luxembourg"),
    MALTA("Malta"),
    MOLDOVA("Moldova"),
    MONACO("Monaco"),
    MONTENEGRO("Montenegro"),
    NETHERLANDS("Netherlands"),
    NORTH_MACEDONIA("North Macedonia"),
    NORWAY("Norway"),
    PORTUGAL("Portugal"),
    ROMANIA("Romania"),
    SAN_MARINO("San Marino"),
    SERBIA("Serbia"),
    SLOVENIA("Slovenia"),
    SPAIN("Spain"),
    SWEDEN("Sweden"),
    SWITZERLAND("Switzerland"),
    TURKEY("Turkey"),
    UNITED("United"),
    VATICAN_CITY("Vatican City"),
    RUSSIA("Russia");

    private final String value;
    private static List<String> listOfCountries = null;

    Countries(String value) {
        this.value = value;
    }


    /***
     * Public API
     * @return
     */
    public static List<String> GetAll_CountriesAsStringList() {
        if (listOfCountries == null || listOfCountries.isEmpty()) {
            listOfCountries = new ArrayList<String>();

            for (Countries ctr : Countries.values()) {
                listOfCountries.add(ctr.value);
            }
        }

        return listOfCountries;
    }


    public String getValue() {
        return value;
    }
}
