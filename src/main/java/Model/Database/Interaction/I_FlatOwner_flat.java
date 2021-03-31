package Model.Database.Interaction;

import Model.Database.Tables.Table.T_FlatOwner_flat;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_FlatOwner_flat {
    // Privates
    private static T_FlatOwner_flat FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_FlatOwner_flat.DBNAME_ID, rs.getInt(T_FlatOwner_flat.DBNAME_ID));
        dict.put(T_FlatOwner_flat.DBNAME_CREATEDAT, rs.getDate(T_FlatOwner_flat.DBNAME_CREATEDAT));

        Date validUntil = rs.getDate(T_FlatOwner_flat.DBNAME_VALIDUNTIL);
        if (null != validUntil)
            dict.put(T_FlatOwner_flat.DBNAME_VALIDUNTIL, validUntil);

        dict.put(T_FlatOwner_flat.DBNAME_FLATOWNERID, rs.getInt(T_FlatOwner_flat.DBNAME_FLATOWNERID));
        dict.put(T_FlatOwner_flat.DBNAME_FLATID, rs.getInt(T_FlatOwner_flat.DBNAME_FLATID));

        return T_FlatOwner_flat.CreateFromRetrieved(rs.getInt(T_FlatOwner_flat.DBNAME_ID), dict);
    }
}
