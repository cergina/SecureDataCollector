package Model.Database.Support.Measuring;

import Model.Database.Interaction.I_CentralUnit;
import Model.Database.Interaction.I_ControllerUnit;
import Model.Database.Interaction.I_Measurements;
import Model.Database.Interaction.I_Sensor;
import Model.Database.Tables.T_CentralUnit;
import Model.Database.Tables.T_ControllerUnit;
import Model.Database.Tables.T_Measurement;
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
        dictMain.put(JSON_CENTRAL_UNIT_DIP, json.getString(JSON_CENTRAL_UNIT_DIP)); // dip address string
        dictMain.put(JSON_REQUEST_NUM, json.getInt(JSON_REQUEST_NUM)); // request number
        dictMain.put(CONSTANTS.TODAYS_DATE, Date.valueOf(LocalDate.now())); // todays date in case the measurements will not be tracked from sender


        ResultSet rsCentral = null;
        T_CentralUnit tcent = I_CentralUnit.retrieveByDip(conn, ps, rsCentral, json.getString(JSON_CENTRAL_UNIT_DIP));
        int centralUnitId = (tcent == null) ? -1 : tcent.getA_pk();

        // get all controllers & loop through 'em
        JSONArray controllersArray = json.getJSONArray(JSON_ARRAY_CONTROLLERS);
        for (int i = 0; i < controllersArray.length(); i++) {

            JSONObject tmpCtrlJson = controllersArray.getJSONObject(i);

            // identify Controller by dip address
            ResultSet rsCtrl = null;
            T_ControllerUnit tctrl = I_ControllerUnit.retrieveByDipAndCentral(conn, ps, rsCtrl, tmpCtrlJson.getString(JSON_CONTROLLER_UNIT_DIP), centralUnitId);
            int controllerId = (tctrl == null) ? -1 : tctrl.getA_pk();


            // get all measurements & loop throuh 'em
            JSONArray measurementsArray = tmpCtrlJson.getJSONArray(JSON_ARRAY_MEASUREMENTS);
            for (int j = 0; j < measurementsArray.length(); j++) {

                JSONObject tmpMsgJson = measurementsArray.getJSONObject(j);

                // find out sensor ID from sensor IO
                ResultSet tmpRs = null;
                int sensorId = I_Sensor.retrieve_ID_by_SensorIO_and_Controller(conn, ps, tmpRs, (String)tmpMsgJson.getString(JSON_MEASUREMENTS_SENSORIO), controllerId);


                Dictionary msgDict = new Hashtable();

                msgDict.put(T_Measurement.DBNAME_VALUE, tmpMsgJson.getInt(JSON_MEASUREMENTS_COUNT));
                msgDict.put(T_Measurement.DBNAME_REQUESTNO, (int)dictMain.get(JSON_REQUEST_NUM));
                msgDict.put(T_Measurement.DBNAME_MEASUREDAT, (Date)dictMain.get(CONSTANTS.TODAYS_DATE));
                msgDict.put(T_Measurement.DBNAME_SENSOR_ID, sensorId);

                // T_Measurement temp object
                T_Measurement tmpMeas = T_Measurement.CreateFromScratch(msgDict);

                // Make an insert
                I_Measurements.insert(conn, ps, tmpMeas);
            }
        }
    }

    private static void HandleErrorMessage(Connection conn, PreparedStatement ps, Measurements_SupportedModes mode, JSONObject json) {
        throw new NotImplementedException();
    }



    private static final String JSON_MESSAGE_TYPE = "messageType";
    private static final String JSON_CENTRAL_UNIT_DIP = "centralUnit";
    private static final String JSON_REQUEST_NUM = "requestNumber";

    private static final String JSON_ARRAY_CONTROLLERS = "controllers";

    private static final String JSON_CONTROLLER_UNIT_DIP = "controllerUnit";

    private static final String JSON_ARRAY_MEASUREMENTS = "measurements";

    private static final String JSON_MEASUREMENTS_SENSORIO = "sensorIO";
    private static final String JSON_MEASUREMENTS_COUNT = "count";
}
