package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_Building;
import Model.Database.Tables.T_FlatOwner_flat;

import java.sql.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

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

    public static List<T_FlatOwner_flat> retrieveByFlatId(Connection conn, PreparedStatement ps, ResultSet rs, int flatId) throws SQLException {
        Assurance.idCheck(flatId);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_FlatOwner_flat.DBTABLE_NAME + " " +
                        "WHERE " + T_FlatOwner_flat.DBNAME_FLATID + "=?"
        );

        int col = 0;
        ps.setInt(++col, flatId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_FlatOwner_flat> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_FlatOwner_flat.FillEntity(rs));
            }
        }

        return arr;
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
