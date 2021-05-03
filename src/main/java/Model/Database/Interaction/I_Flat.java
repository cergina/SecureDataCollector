package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_Flat;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

public class I_Flat extends InteractionWithDatabase {
    public static int insert(Connection conn, PreparedStatement ps, T_Flat tf) throws SQLException {
        if (tf.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Flat is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    T_Flat.DBNAME_APARTMENTNO, T_Flat.DBNAME_BUILDING_ID
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Flat.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, tf.getA_ApartmentNO());
        ps.setInt(++col, tf.getA_BuildingID());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of Flat into db failed.");

        return affectedRows;
    }

    public static List<T_Flat> retrieveByBuildingId(Connection conn, PreparedStatement ps, ResultSet rs, int buildingId) throws SQLException {
        Assurance.idCheck(buildingId);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Flat.DBTABLE_NAME + " " +
                        "WHERE " + T_Flat.DBNAME_BUILDING_ID + "=?"
        );

        int col = 0;
        ps.setInt(++col, buildingId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_Flat> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Flat.FillEntity(rs));
            }
        }

        return arr;
    }

    public static List<T_Flat> retrieveFilteredAll(Connection conn, PreparedStatement ps, ResultSet rs, int buildingId, String apartmentNo) throws SQLException {

        // No Filter is being used
        if (buildingId <= DB_DO_NOT_USE_THIS_FILTER && StringUtils.isNullOrEmpty(apartmentNo)) {
            return InteractionWithDatabase.retrieveAll(conn, ps, rs, DbEntity.ReturnUnusable(T_Flat.class));
        }

        // SQL Definition
        String usedSql = "SELECT " +
                "* " +
                "FROM " + T_Flat.DBTABLE_NAME + " " +
                "WHERE ";


        // add filter rules
        boolean buildingRule = buildingId > 0;
        boolean apartmentRule = StringUtils.isNullOrEmpty(apartmentNo) == false;

        usedSql = (buildingRule ? usedSql + T_Flat.DBTABLE_NAME + ".BuildingID=? " : usedSql);

        /* add second WHERE clause - watch out for possibility of AND requirement if first line rule was added */
        usedSql = (apartmentRule && (buildingRule == false) ? usedSql + T_Flat.DBTABLE_NAME + ".ApartmentNO=? " : usedSql);
        usedSql = (apartmentRule && buildingRule ? usedSql + " AND " + T_Flat.DBTABLE_NAME + ".ApartmentNO=? " : usedSql);

        usedSql += "ORDER BY ID asc";

        // prepare SQL
        ps = conn.prepareStatement(
                usedSql
        );

        int col = 0;
        if (buildingRule)
            ps.setInt(++col, buildingId);
        if (apartmentRule)
            ps.setString(++col, apartmentNo);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_Flat> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Flat.FillEntity(rs));
            }
        }

        return arr;
    }


    // Privates
    public static T_Flat FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_Flat.DBNAME_APARTMENTNO, rs.getString(T_Flat.DBNAME_APARTMENTNO));
        dict.put(T_Flat.DBNAME_BUILDING_ID, rs.getInt(T_Flat.DBNAME_BUILDING_ID));

        return T_Flat.CreateFromRetrieved(rs.getInt(T_Flat.DBNAME_ID), dict);
    }
}
