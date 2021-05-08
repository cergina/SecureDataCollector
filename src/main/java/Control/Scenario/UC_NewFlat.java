package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Flat;
import Model.Database.Interaction.I_FlatOwner;
import Model.Database.Interaction.I_FlatOwner_flat;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.Assurance;
import Model.Database.Tables.T_Flat;
import Model.Database.Tables.T_FlatOwner;
import Model.Database.Tables.T_FlatOwner_flat;
import Model.Web.FlatOwner;
import Model.Web.JsonResponse;
import Model.Web.Specific.Flat_FlatOwners_Creation;
import View.Support.CustomExceptions.CreationException;
import com.mysql.cj.util.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class UC_NewFlat {
    private final DbProvider db;

    public UC_NewFlat(@NotNull DbProvider dbProvider) {
        this.db = dbProvider;
    }

    /***
     * USED FOR FLAT AND FLAT OWNERS INSIDE OF SPECIFIC BUILDING ADDITION
     * @param creation
     * @return
     */
    public final @NotNull JsonResponse createNewFlat_FlatOwner(@NotNull final Flat_FlatOwners_Creation creation) {
        JsonResponse jsonResponse = new JsonResponse();

        try {
            db.beforeSqlExecution(true);

            // check data
            if (everythingOkForCreation(creation) == false) {
                jsonResponse.setMessage("Something is invalid");
                throw new CreationException("Something is invalid");
            }

            // insert both?
            boolean onlyOneOwner = StringUtils.isNullOrEmpty(creation.getOwner2().getFirstName());

            /*  */
            // Create flat
            Dictionary dict = new Hashtable();

            dict.put(T_Flat.DBNAME_APARTMENTNO, creation.getFlat().getApartmentNO());
            dict.put(T_Flat.DBNAME_BUILDING_ID, creation.getFlat().getBuildingId());


            I_Flat.insert(db.getConn(), db.getPs(), T_Flat.CreateFromScratch(dict));
            int newlyInsertedFlatId = InteractionWithDatabase.retrieveLatestPerConnectionInsertedID(db.getConn(), db.getPs(), db.getRs());


            /*  */
            // Create new flat owner [must]
            dict = new Hashtable();

            dict.put(T_FlatOwner.DBNAME_BEFORETITLE, creation.getOwner1().getTitle());
            dict.put(T_FlatOwner.DBNAME_FIRSTNAME, creation.getOwner1().getFirstName());
            dict.put(T_FlatOwner.DBNAME_MIDDLENAME, creation.getOwner1().getMiddleName());
            dict.put(T_FlatOwner.DBNAME_LASTNAME, creation.getOwner1().getLastName());
            dict.put(T_FlatOwner.DBNAME_PHONE, creation.getOwner1().getPhone());
            dict.put(T_FlatOwner.DBNAME_EMAIL, creation.getOwner1().getEmail());
            dict.put(T_FlatOwner.DBNAME_ADDRESS, creation.getOwner1().getAddress());

            I_FlatOwner.insert(db.getConn(), db.getPs(), T_FlatOwner.CreateFromScratch(dict));

            int newlyInsertedFlatOwner1 = InteractionWithDatabase.retrieveLatestPerConnectionInsertedID(db.getConn(), db.getPs(), db.getRs());


            dict = new Hashtable();

            dict.put(T_FlatOwner_flat.DBNAME_FLATOWNERID, newlyInsertedFlatOwner1);
            dict.put(T_FlatOwner_flat.DBNAME_FLATID, newlyInsertedFlatId);

            I_FlatOwner_flat.insert(db.getConn(), db.getPs(), T_FlatOwner_flat.CreateFromScratch(dict));

            /*  */
            // Create new flat owner [opt]
            int newlyInsertedFlatOwner2 = -1;
            if (onlyOneOwner == false) {
                dict = new Hashtable();

                dict.put(T_FlatOwner.DBNAME_BEFORETITLE, creation.getOwner2().getTitle());
                dict.put(T_FlatOwner.DBNAME_FIRSTNAME, creation.getOwner2().getFirstName());
                dict.put(T_FlatOwner.DBNAME_MIDDLENAME, creation.getOwner2().getMiddleName());
                dict.put(T_FlatOwner.DBNAME_LASTNAME, creation.getOwner2().getLastName());
                dict.put(T_FlatOwner.DBNAME_PHONE, creation.getOwner2().getPhone());
                dict.put(T_FlatOwner.DBNAME_EMAIL, creation.getOwner2().getEmail());
                dict.put(T_FlatOwner.DBNAME_ADDRESS, creation.getOwner2().getAddress());

                I_FlatOwner.insert(db.getConn(), db.getPs(), T_FlatOwner.CreateFromScratch(dict));

                newlyInsertedFlatOwner2 = InteractionWithDatabase.retrieveLatestPerConnectionInsertedID(db.getConn(), db.getPs(), db.getRs());

                dict = new Hashtable();

                dict.put(T_FlatOwner_flat.DBNAME_FLATOWNERID, newlyInsertedFlatOwner2);
                dict.put(T_FlatOwner_flat.DBNAME_FLATID, newlyInsertedFlatId);

                I_FlatOwner_flat.insert(db.getConn(), db.getPs(), T_FlatOwner_flat.CreateFromScratch(dict));

            }

            // Success
            db.afterOkSqlExecution();

            jsonResponse.setStatus(HttpServletResponse.SC_CREATED);
            jsonResponse.setMessage("Flat + FlatOwner(s) created.");
        } catch (SQLException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.setMessage("Internal server error.");
        } catch (CreationException e) {
            db.afterExceptionInSqlExecution(e);
            jsonResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.setMessage(e.getMessage());
        }

        return jsonResponse;
    }

    // privates

    /***
     * This is for regular flat and flat owners creation
     * @param creation
     * @return
     * @throws NumberFormatException
     */
    private boolean everythingOkForCreation(@NotNull final Flat_FlatOwners_Creation creation) throws NumberFormatException, SQLException, CreationException {
        // data of flatowners are ok

        if (isFlatOwnerOk(creation.getOwner1()) == false) {
            return false;
        }

        // if second user is provided for enter check him as well
        if (StringUtils.isNullOrEmpty(creation.getOwner2().getFirstName()) == false) {
            if (isFlatOwnerOk(creation.getOwner2()) == false) {
                return false;
            }
        }

        // apartment no cant be empty
        if (StringUtils.isNullOrEmpty(creation.getFlat().getApartmentNO())) {
            return false;
        }

        // is building id and appartmentNo unique?
        if (doesBuildingAlreadyContainSuchApartmentNo(creation.getFlat().getBuildingId(), creation.getFlat().getApartmentNO())) {
            throw new CreationException("Building already contains an apartment with such Apartment NO");
        }

        return Assurance.isFkOk(creation.getFlat().getBuildingId()) != false;
    }

    private boolean isFlatOwnerOk(FlatOwner fo) throws CreationException, SQLException{
        // emails are valid
        if (EmailValidator.getInstance().isValid(fo.getEmail()) == false) {
            return false;
        }

        if (doesFlatOwnerWithSuchEmailAlreadyExist(fo.getEmail())) {
            throw new CreationException("Database already contains flat owner with such email.");
        }

        return !StringUtils.isNullOrEmpty(fo.getFirstName()) && !StringUtils.isNullOrEmpty(fo.getLastName()) &&
                !StringUtils.isNullOrEmpty(fo.getPhone()) && fo.getPhone().matches("^(\\+)?[0-9 ]+$") != false;
    }

    private boolean doesBuildingAlreadyContainSuchApartmentNo(Integer buildingId, String apartmentNo) throws SQLException {
        return I_Flat.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), buildingId, apartmentNo).size() > 0;
    }

    private boolean doesFlatOwnerWithSuchEmailAlreadyExist(String mail) throws SQLException {
        return I_FlatOwner.retrieveFilteredAll(db.getConn(), db.getPs(), db.getRs(), mail).size() > 0;
    }
}
