package Model.Database.Tables;

import Model.Database.Interaction.I_SensorType;
import Model.Database.Support.Assurance;
import Model.Database.Support.DBEnum;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.List;

public class E_SensorType extends DbEntity implements DBEnum, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "sensorType" : "sensortype";

    // Atributes
    private int a_pk;
    private String a_Name;
    private String a_MeasuredIn;
    private int a_CommTypeID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_NAME = "Name";
    public static final String DBNAME_MEASUREDIN = "MeasuredIn";
    public static final String DBNAME_COMMTYPE_ID = "CommTypeID";

    public static E_SensorType REFERENCE = new E_SensorType();
    public static String[] TABLE_CODENAMES = {
            "Name", "Measured In (Units)", "Communication Type ID"
    };

    // Constructors
    protected E_SensorType() {}

    // Creations
    public static E_SensorType CreateFromRetrieved(int pk, Dictionary tmpDict) {
        E_SensorType temp = CreateBase(tmpDict);

        temp.a_pk = pk;

        return temp;
    }

    public static E_SensorType CreateFromScratch(Dictionary tmpDict) {
        return CreateBase(tmpDict);
    }

    private static E_SensorType CreateBase(Dictionary tmpDict) {
        E_SensorType temp = new E_SensorType();

        temp.a_Name = (String)tmpDict.get(DBNAME_NAME);
        temp.a_MeasuredIn = (String)tmpDict.get(DBNAME_MEASUREDIN);
        temp.a_CommTypeID = (int)tmpDict.get(DBNAME_COMMTYPE_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(E_SensorType tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_NAME, tmp.getA_Name());
        jo.put(DBNAME_MEASUREDIN, tmp.getA_MeasuredIn());
        jo.put(DBNAME_COMMTYPE_ID, tmp.getA_CommTypeID());

        return jo;
    }

    // From DbEntity
    public E_SensorType FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return I_SensorType.FillEntity(rs);
    }

    @Override
    public String GetDbTableName() {
        return DBTABLE_NAME;
    }

    // Interface specific
    @Override
    public boolean IsEnumTableOkForDatabaseEnter() {
        return Assurance.isVarcharOk(a_Name) &&
                Assurance.isVarcharOk(a_MeasuredIn) &&
                Assurance.isFkOk(a_CommTypeID);
    }

    @Override
    public boolean WasEnumTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isVarcharOk(a_Name) &&
                Assurance.isVarcharOk(a_MeasuredIn) &&
                Assurance.isFkOk(a_CommTypeID);
    }

    @Override
    public String PrintInfoAboutEnum() {
        throw new NotImplementedException();
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

        str.add(this.a_Name);
        str.add(this.a_MeasuredIn);
        str.add(Integer.toString(a_CommTypeID));

        return str;
    }

    @Override
    public String[] GetTableCodeNames() {
        return TABLE_CODENAMES;
    }

    // Generic
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");

        return str.toString();
    }


    // Getters

    public int getA_pk() {
        return a_pk;
    }

    public String getA_Name() {
        return a_Name;
    }

    public String getA_MeasuredIn() {
        return a_MeasuredIn;
    }

    public int getA_CommTypeID() {
        return a_CommTypeID;
    }
}
