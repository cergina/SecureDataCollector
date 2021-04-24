package Model.Database.Interaction;

import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.E_AccessPrivilege;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_AccessPrivillege {
    public static int insert(Connection conn, PreparedStatement ps, E_AccessPrivilege ec) throws SQLException {
        if (ec.IsEnumTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute E_AccessPrivilege is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    E_AccessPrivilege.DBNAME_NAME
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        E_AccessPrivilege.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, ec.getA_Name());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of AccessPrivilege into db failed.");

        return affectedRows;
    }


    public static E_AccessPrivilege FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(E_AccessPrivilege.DBNAME_NAME, rs.getString(E_AccessPrivilege.DBNAME_NAME));

        return  E_AccessPrivilege.CreateFromRetrieved(rs.getInt(E_AccessPrivilege.DBNAME_ID), dict);
    }
}
