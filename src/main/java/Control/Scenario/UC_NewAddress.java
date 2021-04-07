package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Address;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.Table.T_Address;
import Model.Web.JsonResponse;
import Model.Web.Specific.AddressCreation;
import View.Support.CustomExceptions.AddressCreationException;
import View.Support.CustomExceptions.AlreadyExistsException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;


public class UC_NewAddress {
    private DbProvider db;

    public UC_NewAddress(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    public final @NotNull JsonResponse createNewAddress(@NotNull final AddressCreation addressCreation){
        JsonResponse jsonResponse = new JsonResponse();

        try {
            if (isDataValid(addressCreation) == false) {
                jsonResponse.setMessage("Some required fields are missing.");
                throw new AddressCreationException("Some required fields are missing.");
            }

            if(checkIfAddressExists(addressCreation.getStreet(), addressCreation.getHouseNo(), addressCreation.getCity(), addressCreation.getZIP(), addressCreation.getCountry())){
                throw new AlreadyExistsException("Address already exists.");
            }

            db.beforeSqlExecution(true);
            insertAddressIntoDatabase(addressCreation);

            db.afterOkSqlExecution();
            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Address created.");

        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);
        } catch (AddressCreationException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Address already exists.");
        }

        return jsonResponse;
    }

    private boolean isDataValid(@NotNull final AddressCreation addressCreation) {

        if (addressCreation == null || addressCreation.getStreet().equals("") || addressCreation.getHouseNo().equals("")
                || addressCreation.getCity().equals("") || addressCreation.getZIP().equals("") || addressCreation.getCountry().equals(""))
            return false;

        return true;
    }

    private void insertAddressIntoDatabase(@NotNull final AddressCreation addressCreation) throws SQLException{
        Dictionary dict = new Hashtable();
        dict.put(T_Address.DBNAME_CITY, addressCreation.getCity());
        dict.put(T_Address.DBNAME_COUNTRY, addressCreation.getCountry());
        dict.put(T_Address.DBNAME_HOUSENO, addressCreation.getHouseNo());
        dict.put(T_Address.DBNAME_STREET, addressCreation.getStreet());
        dict.put(T_Address.DBNAME_ZIP, addressCreation.getZIP());
        I_Address.insert(db.getConn(), db.getPs(), T_Address.CreateFromScratch(dict));
    }

    private boolean checkIfAddressExists(final String street, final String houseno, final String city, final String zip, final String country) {
        boolean exists;

        try {
            exists = I_Address.checkIfExists(db.getConn(), db.getPs(), db.getRs(), street, houseno, city, zip, country);
            return exists;
        } catch (SQLException sqle) {
            CustomLogs.Error(sqle.getMessage());
        }

        return true;
    }

}