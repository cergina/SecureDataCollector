package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_CentralUnit;
import Model.Database.Interaction.I_ControllerUnit;
import Model.Database.Interaction.I_Flat;
import Model.Database.Support.Assurance;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.T_CentralUnit;
import Model.Database.Tables.T_ControllerUnit;
import Model.Database.Tables.T_Flat;
import Model.Web.JsonResponse;
import Model.Web.Specific.ControllerCreation;
import View.Support.CustomExceptions.AlreadyExistsException;
import View.Support.CustomExceptions.CreationException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class UC_Controller {
    private DbProvider db;

    public UC_Controller(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }


    public final @NotNull JsonResponse createControllerUnit(@NotNull final ControllerCreation creation){
        JsonResponse jsonResponse = new JsonResponse();

        try {
            if (isDataValid(creation) == false) {
                jsonResponse.setMessage("Some required fields are missing or incorrect.");
                throw new CreationException("Some required fields are missing or incorrect.");
            }

            if(checkIfAlreadyExists(creation.getUid(), creation.getDipAddress(), creation.getFlatId())){
                throw new AlreadyExistsException("Controller Unit already exists.");
            }

            db.beforeSqlExecution(true);
            insertIntoDatabase(creation);

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
            jsonResponse.setMessage("Internal server error.");
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


    private boolean isDataValid(@NotNull final ControllerCreation creation) throws NumberFormatException{
        // dip address can only be between min 1 and max 255
        int temp = Integer.parseInt(creation.getDipAddress());
        if (temp < 1 || temp > 255)
            return false;

        // just to see if its an integer it will throw an exception
        temp = creation.getUid();
        if (temp < 0)
            return false;

        // other fields cannot be empty
        if (creation == null || creation.getZwave().equals("") ||
                creation.getDipAddress().equals("") || Assurance.isFkOk(creation.getFlatId()) == false)
            return false;

        return true;
    }

    private void insertIntoDatabase(@NotNull final ControllerCreation creation) throws SQLException{
        Dictionary dict = new Hashtable();

        dict.put(T_ControllerUnit.DBNAME_UID, creation.getUid());
        dict.put(T_ControllerUnit.DBNAME_DIPADDRESS, creation.getDipAddress());
        dict.put(T_ControllerUnit.DBNAME_ZWAVE, creation.getZwave());

        T_CentralUnit tc = get_TCentralUnit_ByFlatId(creation.getFlatId());
        int centralUnitId = (tc != null ? tc.getA_pk() : -1);

        dict.put(T_ControllerUnit.DBNAME_CENTRALUNIT_ID, centralUnitId);
        dict.put(T_ControllerUnit.DBNAME_FLAT_ID, creation.getFlatId());

        I_ControllerUnit.insert(db.getConn(), db.getPs(), T_ControllerUnit.CreateFromScratch(dict));
    }

    private boolean checkIfAlreadyExists(final int uid, final String dip, final int flatId) {
        boolean exists;

        try {
            exists = I_ControllerUnit.checkIfExists(db.getConn(), db.getPs(), db.getRs(), uid, dip, flatId);
            return exists;
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return true;
    }

    private T_CentralUnit get_TCentralUnit_ByFlatId(@NotNull Integer flatId) {
        T_CentralUnit t = null;

        try {
            // get precise flat
            T_Flat flat = I_Flat.retrieve(db.getConn(), db.getPs(), db.getRs(), flatId);

            // get address id from flat
            int addressId = (flat != null ? flat.getA_AddressID() : -1);

            // get centralUnit that has addressId same
            t = I_CentralUnit.retrieveByAddressId(db.getConn(), db.getPs(), db.getRs(), addressId);
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return t;
    }
}
