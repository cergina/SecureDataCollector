package Model.Measuring;

import Database.Interaction.Entities.Sensor;
import Database.Tables.T_Measurement;
import Model.Shared.CONSTANTS;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.*;
import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Hashtable;

/***
 * This class was created, because to put everything from this logic into POST_Measurements_Receive would be bad
 */
public class Measurements_Process {

    public static void HandleFromPost(Connection conn, PreparedStatement ps, Measurements_SupportedModes mode, JSONObject json) throws SQLException {
        try {
            conn.setAutoCommit(false);

            switch (mode) {
                case REGULAR_MESSAGE:
                    HandleRegularMessage(conn, ps, mode, json);
                    break;
                case ERROR_MESSAGE:
                    HandleErrorMessage(conn, ps, mode, json);
                default:
                    // Will not happen
            }

        } catch (SQLException sqlE) {
             conn.rollback();
             conn.setAutoCommit(true);
             throw sqlE;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private static void HandleRegularMessage(Connection conn, PreparedStatement ps, Measurements_SupportedModes mode, JSONObject json) throws SQLException {

        Dictionary dictMain = new Hashtable();

        dictMain.put(JSON_MESSAGE_TYPE, mode);
        dictMain.put(JSON_CENTRAL_UNIT, json.getInt(JSON_CENTRAL_UNIT));
        dictMain.put(JSON_REQUEST_NUM, json.getInt(JSON_REQUEST_NUM));
        dictMain.put(CONSTANTS.TODAYS_DATE, Date.valueOf(LocalDate.now()));


        // get all controllers & loop through 'em
        JSONArray controllersArray = json.getJSONArray(JSON_ARRAY_CONTROLLERS);
        for (int i = 0; i < controllersArray.length(); i++) {

            JSONObject tmpCtrlJson = controllersArray.getJSONObject(i);

            // identify Controller
            tmpCtrlJson.getInt(JSON_CONTROLLER_UNIT);

            // get all measurements & loop throuh 'em
            JSONArray measurementsArray = tmpCtrlJson.getJSONArray(JSON_ARRAY_MEASUREMENTS);
            for (int j = 0; j < measurementsArray.length(); j++) {

                JSONObject tmpMsgJson = measurementsArray.getJSONObject(j);

                // find out sensor ID from sensor IO
                ResultSet tmpRs = null;
                int sensorId = Sensor.retrieve_ID_by_SensorIO(conn, ps, tmpRs, (String)tmpMsgJson.getString(JSON_MEASUREMENTS_SENSORIO));


                Dictionary msgDict = new Hashtable();

                msgDict.put(T_Measurement.DBNAME_VALUE, tmpMsgJson.getInt(JSON_MEASUREMENTS_COUNT));
                msgDict.put(T_Measurement.DBNAME_MEASUREDINCREMENT, (int)dictMain.get(JSON_REQUEST_NUM));
                msgDict.put(T_Measurement.DBNAME_MEASUREDAT, (Date)dictMain.get(CONSTANTS.TODAYS_DATE));
                msgDict.put(T_Measurement.DBNAME_SENSOR_ID, sensorId);

                // T_Measurement temp object
                T_Measurement tmpMeas = T_Measurement.CreateFromScratch(msgDict);

                // Make an insert
                Database.Interaction.Entities.Measurements.insert(conn, ps, tmpMeas);
            }
        }
    }

    private static void HandleErrorMessage(Connection conn, PreparedStatement ps, Measurements_SupportedModes mode, JSONObject json) {
        throw new NotImplementedException();
    }



    private static final String JSON_MESSAGE_TYPE = "messageType";
    private static final String JSON_CENTRAL_UNIT = "centralUnit";
    private static final String JSON_REQUEST_NUM = "requestNumber";

    private static final String JSON_ARRAY_CONTROLLERS = "controllers";

    private static final String JSON_CONTROLLER_UNIT = "controllerUnit";

    private static final String JSON_ARRAY_MEASUREMENTS = "measurements";

    private static final String JSON_MEASUREMENTS_SENSORIO = "sensorIO";
    private static final String JSON_MEASUREMENTS_COUNT = "count";
}
