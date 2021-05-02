package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_CentralUnit;
import Model.Database.Interaction.I_ControllerUnit;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.Assurance;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_CentralUnit;
import Model.Database.Tables.T_ControllerUnit;
import Model.Database.Tables.T_Flat;
import Model.Web.ControllerUnit;
import Model.Web.JsonResponse;
import View.Support.CustomExceptions.AlreadyExistsException;
import View.Support.CustomExceptions.CreationException;
import com.mysql.cj.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class UC_NewControllerUnit {
    public static final String BUILDING_REQUIRES_CENTRALUNIT = "BuildingRequiresCentralUnit";
    private final DbProvider db;

    public UC_NewControllerUnit(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }


    public final @NotNull JsonResponse createControllerUnit(@NotNull final ControllerUnit controllerUnit){
        JsonResponse jsonResponse = new JsonResponse();

        try {
            if (isDataValid(controllerUnit) == false) {
                jsonResponse.setMessage("Some required fields are missing or incorrect.");
                throw new CreationException("Some required fields are missing or incorrect.");
            }

            if(checkIfAlreadyExists(controllerUnit)){
                throw new AlreadyExistsException("Controller Unit already exists.");
            }

            db.beforeSqlExecution(true);
            insertIntoDatabase(controllerUnit);

            db.afterOkSqlExecution();
            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Controller Unit created.");

        } catch (NumberFormatException ne) {
            db.afterExceptionInSqlExecution(ne);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage("UID and DIP can be only a number.");
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage(e.getMessage().equals(BUILDING_REQUIRES_CENTRALUNIT) ? "You have to first create a central unit for the building, this flat is in." : "Internal server error.");
        } catch (CreationException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage("Controller Unit already exists.");
        }

        return jsonResponse;
    }


    private boolean isDataValid(@NotNull final ControllerUnit controllerUnit) throws NumberFormatException{
        // dip address can only be between min 1 and max 255
        int temp = Integer.parseInt(controllerUnit.getDipAddress());
        if (temp < 1 || temp > 255)
            return false;

        // just to see if its an integer it will throw an exception
        temp = controllerUnit.getUid();
        if (temp < 0)
            return false;

        // other fields cannot be empty
        return !StringUtils.isNullOrEmpty(controllerUnit.getDipAddress()) &&
                !StringUtils.isNullOrEmpty(controllerUnit.getZwave()) &&
                Assurance.isFkOk(controllerUnit.getFlatId()) != false;
    }

    private void insertIntoDatabase(@NotNull final ControllerUnit controllerUnit) throws SQLException{
        Dictionary dict = new Hashtable();

        dict.put(T_ControllerUnit.DBNAME_UID, controllerUnit.getUid());
        dict.put(T_ControllerUnit.DBNAME_DIPADDRESS, controllerUnit.getDipAddress());
        dict.put(T_ControllerUnit.DBNAME_ZWAVE, controllerUnit.getZwave());

        T_CentralUnit tc = get_TCentralUnit_ByFlatId(controllerUnit.getFlatId());
        if (tc == null)
            throw new SQLException(BUILDING_REQUIRES_CENTRALUNIT);

        int centralUnitId = (tc != null ? tc.getA_pk() : -1);

        dict.put(T_ControllerUnit.DBNAME_CENTRALUNIT_ID, centralUnitId);
        dict.put(T_ControllerUnit.DBNAME_FLAT_ID, controllerUnit.getFlatId());

        I_ControllerUnit.insert(db.getConn(), db.getPs(), T_ControllerUnit.CreateFromScratch(dict));
    }

    private boolean checkIfAlreadyExists(@NotNull final ControllerUnit controllerUnit) {
        boolean exists;

        try {
            exists = I_ControllerUnit.checkIfExists(db.getConn(), db.getPs(), db.getRs(), controllerUnit.getUid(), controllerUnit.getDipAddress(), controllerUnit.getFlatId());
            return exists;
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return true;
    }

    private T_CentralUnit get_TCentralUnit_ByFlatId(@NotNull Integer flatId) {
        T_CentralUnit t = null;

        try {
            T_Flat flat = InteractionWithDatabase.retrieve(db.getConn(), db.getPs(), db.getRs(), DbEntity.ReturnUnusable(T_Flat.class), flatId);

            int buildingId = (flat != null ? flat.getA_BuildingID() : -1);

            // there is no more than 1 central unit per flat
            List<T_CentralUnit> listToCheck = I_CentralUnit.retrieveByBuildingId(db.getConn(), db.getPs(), db.getRs(), buildingId);
            if (listToCheck.isEmpty() == false) {
                t = listToCheck.get(0);
            }
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }
}
