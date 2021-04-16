package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.*;
import Model.Database.Support.Assurance;
import Model.Database.Tables.Table.*;
import Model.Web.JsonResponse;
import Model.Web.Specific.FlatFirstTimeCreation;
import Model.Web.Specific.FlatOwner;
import View.Support.CustomExceptions.CreationException;
import org.apache.commons.validator.routines.EmailValidator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

public class UC_CreateFlat {
    private DbProvider db;

    public UC_CreateFlat(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }


    public final @NotNull JsonResponse createNewFlat_Owner_Controller(@NotNull final FlatFirstTimeCreation firstTimeCreation) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            // check data
            if (everythingOkForCreation(firstTimeCreation) == false) {
                throw new CreationException("Something is invalid");
            }

            // insert both?
            boolean onlyOneOwner = firstTimeCreation.getOwner2().getFirstName().equals("");

            db.beforeSqlExecution(true);

            // dip has to be non repetitive for central unit
            if (dipControllerAlreadyPresentForCentralUnit(firstTimeCreation.getCentralUnitId(), firstTimeCreation.getDip())) {
                throw new CreationException("DIP address is already present for central unit.");
            }

            // get central unit
            T_CentralUnit t_centralUnit = I_CentralUnit.retrieve(db.getConn(), db.getPs(), db.getRs(), firstTimeCreation.getCentralUnitId());

            /*  */
            // Create flat
            Dictionary dict = new Hashtable();

            dict.put(T_Flat.DBNAME_APARTMENTNO, firstTimeCreation.getApartmentNo());
            dict.put(T_Flat.DBNAME_ADDRESS_ID, t_centralUnit.getA_AddressID());


            I_Flat.insert(db.getConn(), db.getPs(), T_Flat.CreateFromScratch(dict));
            int newlyInsertedFlatId = I_Flat.retrieveLatestPerConnectionInsertedID(db.getConn(), db.getPs(), db.getRs());


            /*  */
            // Create new flat owner [must]
            dict = new Hashtable();

            dict.put(T_FlatOwner.DBNAME_BEFORETITLE, firstTimeCreation.getOwner1().getTitle());
            dict.put(T_FlatOwner.DBNAME_FIRSTNAME, firstTimeCreation.getOwner1().getFirstName());
            dict.put(T_FlatOwner.DBNAME_MIDDLENAME, firstTimeCreation.getOwner1().getMiddleName());
            dict.put(T_FlatOwner.DBNAME_LASTNAME, firstTimeCreation.getOwner1().getLastName());
            dict.put(T_FlatOwner.DBNAME_PHONE, firstTimeCreation.getOwner1().getPhone());
            dict.put(T_FlatOwner.DBNAME_EMAIL, firstTimeCreation.getOwner1().getEmail());
            dict.put(T_FlatOwner.DBNAME_ADDRESS, firstTimeCreation.getOwner1().getAddress());

            I_FlatOwner.insert(db.getConn(), db.getPs(), T_FlatOwner.CreateFromScratch(dict));

            int newlyInsertedFlatOwner1 = I_FlatOwner.retrieveLatestPerConnectionInsertedID(db.getConn(), db.getPs(), db.getRs());


            dict = new Hashtable();

            dict.put(T_FlatOwner_flat.DBNAME_FLATOWNERID, newlyInsertedFlatOwner1);
            dict.put(T_FlatOwner_flat.DBNAME_FLATID, newlyInsertedFlatId);

            I_FlatOwner_flat.insert(db.getConn(), db.getPs(), T_FlatOwner_flat.CreateFromScratch(dict));

            /*  */
            // Create new flat owner [opt]
            int newlyInsertedFlatOwner2 = -1;
            if (onlyOneOwner == false) {
                dict = new Hashtable();

                dict.put(T_FlatOwner.DBNAME_BEFORETITLE, firstTimeCreation.getOwner2().getTitle());
                dict.put(T_FlatOwner.DBNAME_FIRSTNAME, firstTimeCreation.getOwner2().getFirstName());
                dict.put(T_FlatOwner.DBNAME_MIDDLENAME, firstTimeCreation.getOwner2().getMiddleName());
                dict.put(T_FlatOwner.DBNAME_LASTNAME, firstTimeCreation.getOwner2().getLastName());
                dict.put(T_FlatOwner.DBNAME_PHONE, firstTimeCreation.getOwner2().getPhone());
                dict.put(T_FlatOwner.DBNAME_EMAIL, firstTimeCreation.getOwner2().getEmail());
                dict.put(T_FlatOwner.DBNAME_ADDRESS, firstTimeCreation.getOwner2().getAddress());

                I_FlatOwner.insert(db.getConn(), db.getPs(), T_FlatOwner.CreateFromScratch(dict));

                newlyInsertedFlatOwner2 = I_FlatOwner.retrieveLatestPerConnectionInsertedID(db.getConn(), db.getPs(), db.getRs());

                dict = new Hashtable();

                dict.put(T_FlatOwner_flat.DBNAME_FLATOWNERID, newlyInsertedFlatOwner2);
                dict.put(T_FlatOwner_flat.DBNAME_FLATID, newlyInsertedFlatId);

                I_FlatOwner_flat.insert(db.getConn(), db.getPs(), T_FlatOwner_flat.CreateFromScratch(dict));

            }

            /*  */
            // Create new ControllerUnit
            dict = new Hashtable();

            dict.put(T_ControllerUnit.DBNAME_UID, firstTimeCreation.getUid());
            dict.put(T_ControllerUnit.DBNAME_DIPADDRESS, firstTimeCreation.getDip());
            dict.put(T_ControllerUnit.DBNAME_ZWAVE, firstTimeCreation.getZwave());
            dict.put(T_ControllerUnit.DBNAME_CENTRALUNIT_ID, firstTimeCreation.getCentralUnitId());
            dict.put(T_ControllerUnit.DBNAME_FLAT_ID, newlyInsertedFlatId);

            I_ControllerUnit.insert(db.getConn(), db.getPs(), T_ControllerUnit.CreateFromScratch(dict));

            // Success
            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Flat + FlatOwner(s) and Controller created.");
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");

        } catch (CreationException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage("Testing");
        }

        return jsonResponse;
    }

    private boolean dipControllerAlreadyPresentForCentralUnit(int centralUnitId, String dip) throws SQLException {
        List<T_ControllerUnit> list = I_ControllerUnit.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), centralUnitId, DB_DO_NOT_USE_THIS_FILTER, dip);

        if (list.size() > 0) {
            return true;
        }

        return false;
    }


    // privates
    private boolean everythingOkForCreation(@NotNull final FlatFirstTimeCreation firstTimeCreation) throws NumberFormatException {
        // data of flatowners are ok

        if (isOkFlatOwner(firstTimeCreation.getOwner1()) == false) {
            return false;
        }

        // if second user is provided for enter check him as well
        if (firstTimeCreation.getOwner2().getFirstName().equals("") == false) {
            if (isOkFlatOwner(firstTimeCreation.getOwner2()) == false) {
                return false;
            }
        }

        // uid cant exist - secured by unique mysql
        if (firstTimeCreation.getUid() < 1) {
            return false;
        }

        // dip address has to be unique for central unit
        int temp = Integer.parseInt(firstTimeCreation.getDip());
        if (temp < 1 || temp > 255)
            return false;


        // apartment no cant be empty
        if (firstTimeCreation.getApartmentNo().equals("")) {
            return false;
        }

        if (firstTimeCreation.getZwave().equals("")) {
            return false;
        }

        if (Assurance.isFkOk(firstTimeCreation.getCentralUnitId()) == false) {
            return false;
        }

        return true;
    }

    private boolean isOkFlatOwner(FlatOwner fo) {
        // emails are valid
        if (EmailValidator.getInstance().isValid(fo.getEmail()) == false) {
            return false;
        };

        if (fo.getFirstName().equals("") || fo.getLastName().equals("") || fo.getPhone().matches("^(\\+)?[0-9 ]+$") == false) {
            return false;
        }

        return true;
    }
}
