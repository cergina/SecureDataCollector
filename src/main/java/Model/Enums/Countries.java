package Model.Enums;

import java.util.ArrayList;
import java.util.List;

public enum Countries {
    RUSSIA("Russia"),
    UKRAINE("Ukraine"),
    FRANCE("France"),
    SPAIN("Spain"),
    SWEDEN("Sweden"),
    NORWAY("Norway"),
    GERMANY("Germany"),
    FINLAND("Finland"),
    POLAND("Poland"),
    ITALY("Italy"),
    UNITED("United"),
    ROMANIA("Romania"),
    BELARUS("Belarus"),
    KAZAKHSTAN("Kazakhstan"),
    GREECE("Greece"),
    BULGARIA("Bulgaria"),
    ICELAND("Iceland"),
    HUNGARY("Hungary"),
    PORTUGAL("Portugal"),
    AUSTRIA("Austria"),
    CZECH_REPUBLIC("Czech Republic"),
    SERBIA("Serbia"),
    IRELAND("Ireland"),
    LITHUANIA("Lithuania"),
    LATVIA("Latvia"),
    CROATIA("Croatia"),
    BOSNIA_AND_HERCEGOVINA("Bosnia and Hercegovina"),
    SLOVAKIA("Slovakia"),
    ESTONIA("Estonia"),
    DENMARK("Denmark"),
    SWITZERLAND("Switzerland"),
    NETHERLANDS("Netherlands"),
    MOLDOVA("Moldova"),
    BELGIUM("Belgium"),
    ARMENIA("Armenia"),
    ALBANIA("Albania"),
    NORTH_MACEDONIA("North Macedonia"),
    TURKEY("Turkey"),
    SLOVENIA("Slovenia"),
    MONTENEGRO("Montenegro"),
    KOSOVO("Kosovo"),
    AZERBAIJAN("Azerbaijan"),
    CYPRUS("Cyprus"),
    LUXEMBOURG("Luxembourg"),
    GEORGIA("Georgia"),
    ANDORRA("Andorra"),
    MALTA("Malta"),
    LIECHTENSTEIN("Liechtenstein"),
    SAN_MARINO("San Marino"),
    MONACO("Monaco"),
    VATICAN_CITY("Vatican City");

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
