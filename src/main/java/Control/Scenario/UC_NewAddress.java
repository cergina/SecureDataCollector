package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Address;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.T_Address;
import Model.Web.JsonResponse;
import Model.Web.Address;
import View.Support.CustomExceptions.AlreadyExistsException;
import View.Support.CustomExceptions.CreationException;
import com.mysql.cj.util.StringUtils;

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

    public final @NotNull JsonResponse createNewAddress(@NotNull final Address address){
        JsonResponse jsonResponse = new JsonResponse();

        try {
            if (isDataValid(address) == false) {
                jsonResponse.setMessage("Some required fields are missing.");
                throw new CreationException("Some required fields are missing.");
            }

            if(checkIfAddressExists(address.getStreet(), address.getHouseno(), address.getCity(), address.getZip(), address.getCountry())){
                throw new AlreadyExistsException("Address already exists.");
            }

            db.beforeSqlExecution(true);
            insertAddressIntoDatabase(address);

            db.afterOkSqlExecution();
            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Address created.");

        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);
        } catch (CreationException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Address already exists.");
        }

        return jsonResponse;
    }

    private boolean isDataValid(@NotNull final Address address) {

        if (address == null)
            return false;


        if (StringUtils.isNullOrEmpty(address.getStreet()) ||
                StringUtils.isNullOrEmpty(address.getHouseno()) ||
                StringUtils.isNullOrEmpty(address.getCity()) ||
                StringUtils.isNullOrEmpty(address.getZip()) ||
                StringUtils.isNullOrEmpty(address.getCountry()))
            return false;

        return true;
    }

    private void insertAddressIntoDatabase(@NotNull final Address address) throws SQLException{
        Dictionary dict = new Hashtable();

        dict.put(T_Address.DBNAME_CITY, address.getCity());
        dict.put(T_Address.DBNAME_COUNTRY, address.getCountry());
        dict.put(T_Address.DBNAME_HOUSENO, address.getHouseno());
        dict.put(T_Address.DBNAME_STREET, address.getStreet());
        dict.put(T_Address.DBNAME_ZIP, address.getZip());

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