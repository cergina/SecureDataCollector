package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_FlatOwner_flat;

import java.sql.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_FlatOwner_flat extends InteractionWithDatabase {
    public static int insert(Connection conn, PreparedStatement ps, T_FlatOwner_flat t) throws SQLException {
        if (t.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_FlatOwner_flat is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                T_FlatOwner_flat.DBNAME_CREATEDAT,
                T_FlatOwner_flat.DBNAME_FLATOWNERID,
                T_FlatOwner_flat.DBNAME_FLATID
        );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_FlatOwner_flat.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setDate(++col, t.getA_CreatedAt());
        ps.setInt(++col, t.getA_FlatOwnerID());
        ps.setInt(++col, t.getA_FlatID());


        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of FlatOwner_flat entry into db failed.");

        return affectedRows;
    }

    /*
        by specific Id, probably will not be used
    */
    public static T_FlatOwner_flat retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.idCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_FlatOwner_flat.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_FlatOwner_flat t = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            t = I_FlatOwner_flat.FillEntity(rs);
        }

        return t;
    }


    // Privates
    public static T_FlatOwner_flat FillEntity(ResultSet rs) throws SQLException {

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
