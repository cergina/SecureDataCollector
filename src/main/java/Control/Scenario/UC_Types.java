package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_CommType;
import Model.Database.Interaction.I_SensorType;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.Enum.E_CommType;
import Model.Database.Tables.Enum.E_SensorType;
import Model.Web.CommType;
import Model.Web.JsonResponse;
import Model.Web.SensorType;
import View.Support.CustomExceptions.AlreadyExistsException;
import View.Support.CustomExceptions.InvalidOperationException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Use Case class for creating database entries by admin
 */
public class UC_Types {
    private DbProvider db;

    public UC_Types(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final @NotNull JsonResponse createCommType(final @NotNull CommType commType) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution(true);

            if (commType.getName().equals("")) {
                jsonResponse.setMessage("Communication type name cannot be empty.");
                throw new InvalidOperationException("Communication type name cannot be empty.");
            }

            if (I_CommType.retrieveByName(db.getConn(), db.getPs(), db.getRs(), commType.getName()) != null) {
                jsonResponse.setMessage("Communication type with this name already exists.");
                throw new AlreadyExistsException("Communication type with this name already exists.");
            }

            // modify Commtype table START
            Dictionary dict_commtype = new Hashtable();
            dict_commtype.put(E_CommType.DBNAME_NAME, commType.getName());

            E_CommType e_commType = E_CommType.CreateFromScratch(dict_commtype);
            if (!e_commType.IsEnumTableOkForDatabaseEnter()) {
                jsonResponse.setMessage("Data does not match database scheme.");
                throw new InvalidOperationException("Data does not match database scheme.");
            }

            I_CommType.insert(db.getConn(), db.getPs(), e_commType);
            // modify Commtype table END

            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Communication type created.");
            jsonResponse.setData(commType);
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

    public final @NotNull JsonResponse createSensorType(final @NotNull SensorType sensorType) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution(true);

            if (sensorType.getName().equals("")) {
                jsonResponse.setMessage("Sensor type name cannot be empty.");
                throw new InvalidOperationException("Sensor type name cannot be empty.");
            }

            if (I_SensorType.retrieveByName(db.getConn(), db.getPs(), db.getRs(), sensorType.getName()) != null) {
                jsonResponse.setMessage("Sensor type with this name already exists.");
                throw new AlreadyExistsException("Sensor type with this name already exists.");
            }

            E_CommType e_commType = I_CommType.retrieveByName(db.getConn(), db.getPs(), db.getRs(), sensorType.getCommType().getName());
            if (e_commType == null) {
                jsonResponse.setMessage("Communication type with this name does not exist.");
                throw new InvalidOperationException("Communication type with this name does not exist.");
            }

            // modify Sensortype table START
            Dictionary dict_sensortype = new Hashtable();
            dict_sensortype.put(E_SensorType.DBNAME_NAME, sensorType.getName());
            dict_sensortype.put(E_SensorType.DBNAME_MEASUREDIN, sensorType.getMeasuredin());
            dict_sensortype.put(E_SensorType.DBNAME_COMMTYPE_ID, e_commType.getA_pk());

            E_SensorType e_sensorType = E_SensorType.CreateFromScratch(dict_sensortype);
            if (!e_sensorType.IsEnumTableOkForDatabaseEnter()) {
                jsonResponse.setMessage("Data does not match database scheme.");
                throw new InvalidOperationException("Data does not match database scheme.");
            }

            I_SensorType.insert(db.getConn(), db.getPs(), e_sensorType);
            // modify Sensortype table END

            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Sensor type created.");
            jsonResponse.setData(sensorType);
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

    public final @NotNull List<CommType> getAll_CommType() {
        List<CommType> commTypeList = new ArrayList<>();

        try {
            List<E_CommType> e_commTypeList = I_CommType.retrieveAll(db.getConn(), db.getPs(), db.getRs());
            for (E_CommType e_commType : e_commTypeList) {
                CommType commType = new CommType(e_commType.getA_pk(), e_commType.getA_Name());
                commTypeList.add(commType);
            }
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return commTypeList;
    }

    public final @NotNull List<SensorType> getAll_SensorType(boolean includeCommTypes) {
        List<SensorType> sensorTypeList = new ArrayList<>();

        List<CommType> commTypeList = includeCommTypes ? getAll_CommType() : null;
        try {
            List<E_SensorType> e_sensorTypeList = I_SensorType.retrieveAll(db.getConn(), db.getPs(), db.getRs());
            for (E_SensorType e_sensorType : e_sensorTypeList) {

                if (includeCommTypes) {
                    List<CommType> commTypes = new ArrayList<>();
                    for (CommType commType : commTypeList) {
                        if (e_sensorType.getA_CommTypeID() == commType.getId()) {
                            commTypes.add(commType);
                        }
                    }
                    SensorType sensorType = new SensorType(e_sensorType.getA_pk(), e_sensorType.getA_Name(), e_sensorType.getA_MeasuredIn(), commTypes);
                    sensorTypeList.add(sensorType);
                } else {
                    SensorType sensorType = new SensorType(e_sensorType.getA_pk(), e_sensorType.getA_Name(), e_sensorType.getA_MeasuredIn());
                    sensorTypeList.add(sensorType);
                }
            }
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return sensorTypeList;
    }
}
