package Model.Database.Tables.Table;

import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import Model.Database.Tables.DbEntity;
import org.json.JSONObject;

import java.sql.Date;
import java.util.Dictionary;
import java.util.List;

public class T_Measurement extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "measurement" : "measurement";

    // Atributes
    private int a_pk;
    private int a_Value;
    private int a_RequestNo;
    private Date a_MeasuredAt;
    private int a_AccumulatedValue;
    private int a_SensorID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_VALUE = "Value";
    public static final String DBNAME_REQUESTNO = "RequestNo";
    public static final String DBNAME_MEASUREDAT = "MeasuredAt";
    public static final String DBNAME_ACCUMULATEDVALUE = "AccumulatedValue";
    public static final String DBNAME_SENSOR_ID = "SensorID";

    public static T_Measurement REFERENCE = new T_Measurement();
    public static String[] TABLE_CODENAMES = {
            "Value", "Request Number", "Accumulated Value", "Measured At", "Sensor ID"
    };

    // Constructors
    private T_Measurement() {}

    // Creations
    public static T_Measurement CreateFromRetrieved(int pk, Dictionary dict) {
        T_Measurement temp = new T_Measurement();

        temp.a_pk = pk;
        temp.a_Value = (int)dict.get(DBNAME_VALUE);
        temp.a_RequestNo = (int)dict.get(DBNAME_REQUESTNO);
        temp.a_AccumulatedValue = (int)dict.get(DBNAME_ACCUMULATEDVALUE);
        temp.a_MeasuredAt = (Date)dict.get(DBNAME_MEASUREDAT);
        temp.a_SensorID = (int)dict.get(DBNAME_SENSOR_ID);

        return temp;
    }

    public static T_Measurement CreateFromScratch(Dictionary dict) {
        T_Measurement temp = new T_Measurement();

        temp.a_Value = (int)dict.get(DBNAME_VALUE);
        temp.a_RequestNo = (int)dict.get(DBNAME_REQUESTNO);
        // This cant be known at this time
        //temp.a_AccumulatedValue = (int)dict.get(DBNAME_ACCUMULATEDVALUE);
        temp.a_MeasuredAt = (Date)dict.get(DBNAME_MEASUREDAT);
        temp.a_SensorID = (int)dict.get(DBNAME_SENSOR_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_Measurement tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_VALUE, tmp.getA_Value());
        jo.put(DBNAME_REQUESTNO, tmp.getA_RequestNo());
        jo.put(DBNAME_ACCUMULATEDVALUE, tmp.getA_AccumulatedValue());
        jo.put(DBNAME_MEASUREDAT, tmp.getA_MeasuredAt());
        jo.put(DBNAME_SENSOR_ID, tmp.getA_SensorID());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsIntOk(a_Value) &&
                Assurance.IsIntOk(a_RequestNo) &&
                // This cant be known at this time
                //Assurance.IsIntOk(a_AccumulatedValue) &&
                Assurance.IsDateOk(a_MeasuredAt) &&
                Assurance.IsFkOk(a_SensorID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsFkOk(a_pk) &&
                Assurance.IsIntOk(a_Value) &&
                Assurance.IsIntOk(a_RequestNo) &&
                Assurance.IsIntOk(a_AccumulatedValue) &&
                Assurance.IsDateOk(a_MeasuredAt) &&
                Assurance.IsFkOk(a_SensorID);
    }

    @Override
    public String InfoPrintAllColumns(){
        return  "id:PK | " +
                "Value: varchar | " +
                "RequestNo: int | " +
                "AccumulatedValue: int | " +
                "MeasuredAt: Date | " +
                "SensorID: int";
    }

    @Override
    public String ReturnDBNamesInHtmlRow() {
        String documentPart = "";
        documentPart += "<tr>";

        for (String str:TABLE_CODENAMES
        ) {
            documentPart += "<th>" + str + "</th>";
        }

        documentPart += "<th></th>";
        documentPart += "</tr>";

        return documentPart;
    }

    // For HTML purposes
    @Override
    public List<String> GenerateHtmlTableRow_FromDbRow() {
        List<String> str = super.GenerateHtmlTableRow_FromDbRow();

        str.add(Integer.toString(a_Value));
        str.add(Integer.toString(a_RequestNo));
        str.add(Integer.toString(a_AccumulatedValue));
        str.add(a_MeasuredAt.toString());
        str.add(Integer.toString(a_SensorID));

        return str;
    }

    @Override
    public String[] GetTableCodeNames() {
        return TABLE_CODENAMES;
    }


    // Generic
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("[");

        if (Assurance.IsFkOk(a_pk))
            str.append(DBNAME_ID + ": " + a_pk + "; ");

        if (Assurance.IsIntOk(a_Value))
            str.append(DBNAME_VALUE + ": " + a_Value + "; ");

        if (Assurance.IsIntOk(a_RequestNo))
            str.append(DBNAME_REQUESTNO + ": " + a_RequestNo + "; ");

        if (Assurance.IsIntOk(a_AccumulatedValue))
            str.append(DBNAME_ACCUMULATEDVALUE + ": " + a_AccumulatedValue + "; ");

        if (Assurance.IsDateOk(a_MeasuredAt))
            str.append(DBNAME_MEASUREDAT + ": " + a_MeasuredAt + "; ");

        if (Assurance.IsIntOk(a_SensorID))
            str.append(DBNAME_SENSOR_ID + ": " + a_SensorID + "; ");

        str.append("]");

        return str.toString();
    }


    // Getters

    public int getA_pk() {
        return a_pk;
    }

    public int getA_Value() {
        return a_Value;
    }

    public int getA_RequestNo() {
        return a_RequestNo;
    }

    public int getA_AccumulatedValue() {
        return a_AccumulatedValue;
    }

    public Date getA_MeasuredAt() {
        return a_MeasuredAt;
    }

    public int getA_SensorID() {
        return a_SensorID;
    }
}
