package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_CentralUnit;
import Model.Database.Support.Assurance;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.T_CentralUnit;
import Model.Web.CentralUnit;
import Model.Web.JsonResponse;
import View.Support.CustomExceptions.AlreadyExistsException;
import View.Support.CustomExceptions.CreationException;
import com.mysql.cj.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class UC_NewCentralUnit {
    private final DbProvider db;

    public UC_NewCentralUnit(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }


    public final @NotNull JsonResponse createCentralUnit(@NotNull final CentralUnit centralUnit){
        JsonResponse jsonResponse = new JsonResponse();

        try {
            if (isDataValid(centralUnit) == false) {
                jsonResponse.setMessage("Some required fields are missing or incorrect.");
                throw new CreationException("Some required fields are missing or incorrect.");
            }

            if(checkIfAlreadyExists(centralUnit)){
                throw new AlreadyExistsException("Central Unit already exists.");
            }

            if(checkIfDipNotFree(centralUnit.getDipAddress())){
                throw new AlreadyExistsException("Dip address of central unit already occupied. Use different DIP address.");
            }

            db.beforeSqlExecution(true);
            insertIntoDatabase(centralUnit);

            db.afterOkSqlExecution();
            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Central Unit created.");

        } catch (NumberFormatException ne) {
            db.afterExceptionInSqlExecution(ne);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage("UID and DIP can be only a number.");
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");
        } catch (CreationException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage(e.getMessage());
        }

        return jsonResponse;
    }

    public final @NotNull JsonResponse assignCentralUnitToBuilding(@NotNull final CentralUnit centralUnit, @NotNull Integer buildingId) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution(true);

            // currently impossible due to database restrictions (only 1 to 1 maping of central unit and building)

            db.afterOkSqlExecution();
            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Central Unit assigned.");
        } catch (Exception e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage("Central Unit already exists.");
        }

        return jsonResponse;
    }

    private boolean isDataValid(@NotNull final CentralUnit centralUnit) throws NumberFormatException{
        // dip address can only be between min 1 and max 255
        int temp = Integer.parseInt(centralUnit.getDipAddress());
        if (temp < 1 || temp > 255)
            return false;

        // just to see if its an integer it will throw an exception
        temp = centralUnit.getUid();
        if (temp < 0)
            return false;

        // other fields cannot be empty
        return !StringUtils.isNullOrEmpty(centralUnit.getDipAddress()) &&
                !StringUtils.isNullOrEmpty(centralUnit.getFriendlyName()) &&
                !StringUtils.isNullOrEmpty(centralUnit.getSimNo()) &&
                !StringUtils.isNullOrEmpty(centralUnit.getImei()) &&
                !StringUtils.isNullOrEmpty(centralUnit.getZwave()) &&
                Assurance.isFkOk(centralUnit.getBuildingId()) != false;
    }

    private void insertIntoDatabase(@NotNull final CentralUnit centralUnit) throws SQLException{
        Dictionary dict = new Hashtable();

        dict.put(T_CentralUnit.DBNAME_UID, centralUnit.getUid());
        dict.put(T_CentralUnit.DBNAME_DIPADDRESS, centralUnit.getDipAddress());
        dict.put(T_CentralUnit.DBNAME_FRIENDLYNAME, centralUnit.getFriendlyName());
        dict.put(T_CentralUnit.DBNAME_SIMNO, centralUnit.getSimNo());
        dict.put(T_CentralUnit.DBNAME_IMEI, centralUnit.getImei());
        dict.put(T_CentralUnit.DBNAME_ZWAVE, centralUnit.getZwave());
        dict.put(T_CentralUnit.DBNAME_BUILDING_ID, centralUnit.getBuildingId());

        I_CentralUnit.insert(db.getConn(), db.getPs(), T_CentralUnit.CreateFromScratch(dict));
    }

    private boolean checkIfAlreadyExists(@NotNull final CentralUnit centralUnit) {
        try {
            return I_CentralUnit.checkIfExists(db.getConn(), db.getPs(), db.getRs(), centralUnit.getUid(), centralUnit.getDipAddress(), centralUnit.getBuildingId());
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return true;
    }

    private boolean checkIfDipNotFree(@NotNull final String dip) {
        try {
            return I_CentralUnit.checkIfDipOccupiedAlready(db.getConn(), db.getPs(), db.getRs(), dip);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return true;
    }
}
