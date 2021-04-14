package Control.Scenario;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Hash;
import Model.Database.Interaction.I_User;
import Model.Database.Tables.Table.T_Hash;
import Model.Database.Tables.Table.T_User;
import Model.Web.Auth;
import Model.Web.User;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;

/*
*  Make sure it stays NON-public. Only the same package will have access to this
* */
class Shared_Uc {
    ////////////////////////////////////////
    //          Authentication            //
    ///////////////////////////////////////

    /**
     * Retrieve user and password hash
     * @param email User to search for
     * @return {@link Auth} instance, null if email not in database
     */
    protected static @NotNull Auth retrieveAuthByEmail(@NotNull final String email, DbProvider db) throws SQLException {

        T_User t_user = I_User.retrieveByEmail(db.getConn(), db.getPs(), db.getRs(), email);
        if (t_user == null) { // isnt email in db?
            return null;
        }

        User user = new User();
        user.setBeforetitle(t_user.getA_BeforeTitle());
        user.setFirstname(t_user.getA_FirstName());
        user.setMiddlename(t_user.getA_MiddleName());
        user.setLastname(t_user.getA_LastName());
        user.setPhone(t_user.getA_Phone());
        user.setEmail(t_user.getA_Email());
        user.setResidence(t_user.getA_PermanentResidence());
        user.setUserID(t_user.getA_pk());

        T_Hash t_hash = I_Hash.retrieveLatest(db.getConn(), db.getPs(), db.getRs(), t_user.getA_pk());

        Auth auth = new Auth();
        auth.setUser(user);
        auth.setPassword(t_hash.getA_Value());

        return auth;
    }

    ////////////////////////////////////////
    //          DACO            //
    ///////////////////////////////////////


    ////////////////////////////////////////
    //          DACO            //
    ///////////////////////////////////////


    ////////////////////////////////////////
    //          DACO            //
    ///////////////////////////////////////


    ////////////////////////////////////////
    //          DACO            //
    ///////////////////////////////////////


    ////////////////////////////////////////
    //          DACO            //
    ///////////////////////////////////////
}
