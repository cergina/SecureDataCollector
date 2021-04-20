package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_ControllerUnit;
import Model.Database.Interaction.I_Sensor;
import Model.Database.Interaction.I_SensorType;
import Model.Database.Tables.E_SensorType;
import Model.Database.Tables.T_Sensor;
import Model.Web.JsonResponse;
import Model.Web.Sensor;
import View.Support.CustomExceptions.AlreadyExistsException;
import View.Support.CustomExceptions.InvalidOperationException;
import com.mysql.cj.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class UC_CreateSensor {
    private DbProvider db;

    public UC_CreateSensor(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final @NotNull JsonResponse createSensor(@NotNull final Sensor sensor){
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution(true);

            if (StringUtils.isNullOrEmpty(sensor.getName())) {
                jsonResponse.setMessage("Sensor name cannot be empty.");
                throw new InvalidOperationException("Sensor name cannot be empty.");
            }

            if (I_Sensor.retrieveByInputAndControllerUnitId(db.getConn(), db.getPs(), db.getRs(), sensor.getInput(), sensor.getControllerUnitId()) != null) {
                jsonResponse.setMessage("Sensor with this input already exists.");
                throw new AlreadyExistsException("Sensor with this input already exists.");
            }

            E_SensorType e_sensorType = I_SensorType.retrieveByName(db.getConn(), db.getPs(), db.getRs(), sensor.getSensorTypeName());
            if (e_sensorType == null) {
                jsonResponse.setMessage("Sensor type with this name does not exist.");
                throw new InvalidOperationException("Sensor type with this name does not exist.");
            }

            if (I_ControllerUnit.retrieve(db.getConn(), db.getPs(), db.getRs(), sensor.getControllerUnitId()) == null) {
                jsonResponse.setMessage("Controller unit with this id does not exist.");
                throw new InvalidOperationException("Controller unit with this id does not exist.");
            }

            // modify Sensor table START
            Dictionary dict_sensor = new Hashtable();
            dict_sensor.put(T_Sensor.DBNAME_INPUT, sensor.getInput());
            dict_sensor.put(T_Sensor.DBNAME_NAME, sensor.getName());
            dict_sensor.put(T_Sensor.DBNAME_SENSORTYPE_ID, e_sensorType.getA_pk());
            dict_sensor.put(T_Sensor.DBNAME_CONTROLLERUNIT_ID, sensor.getControllerUnitId());

            T_Sensor t_sensor = T_Sensor.CreateFromScratch(dict_sensor);
            if (!t_sensor.IsTableOkForDatabaseEnter()) {
                jsonResponse.setMessage("Data does not match database scheme.");
                throw new InvalidOperationException("Data does not match database scheme.");
            }

            I_Sensor.insert(db.getConn(), db.getPs(), t_sensor);
            // modify Sensor table END

            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Sensor created.");
            jsonResponse.setData(sensor);
        } catch (InvalidOperationException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_CONFLICT);
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);

            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");
        }
        return jsonResponse;
    }
}
