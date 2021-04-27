package Model.Database.Interaction.ComplexInteractions;

import Model.Database.Interaction.I_ProjectUser;
import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_Building;
import Model.Database.Tables.T_Flat;
import Model.Database.Tables.T_Project_user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneralAccessibility {
    /****
     * If returned null user has no right to see the project, iow: the combination does not exist
     * @param conn
     * @param ps
     * @param rs
     * @param userId
     * @param flatId
     * @return
     * @throws SQLException
     */
    public static T_Project_user doesUserHaveRightToAccessFlat(Connection conn, PreparedStatement ps, ResultSet rs, int userId, int flatId) throws SQLException {
        Assurance.idCheck(userId);
        Assurance.idCheck(flatId);

        /**
         SELECT * FROM dcs.project_user WHERE dcs.project_user.ProjectID=(
            SELECT dcs.building.ProjectID FROM dcs.building WHERE dcs.building.ID=(
                SELECT dcs.flat.BuildingID FROM dcs.flat WHERE dcs.flat.ID=?
            )
         ) AND dcs.project_user.UserID=?;
         */
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Project_user.DBTABLE_NAME + " " +
                        "WHERE " + T_Project_user.DBTABLE_NAME + ".ProjectID=" +
                            "(SELECT "
                                + T_Building.DBTABLE_NAME + ".ProjectID " +
                                "FROM " + T_Building.DBTABLE_NAME + " " +
                                "WHERE " + T_Building.DBTABLE_NAME + ".ID=(" +
                                    "SELECT " +
                                        T_Flat.DBTABLE_NAME + ".BuildingID " +
                                        "FROM " + T_Flat.DBTABLE_NAME + " " +
                                        "WHERE " + T_Flat.DBTABLE_NAME + ".ID=?)) " +
                        "AND " + T_Project_user.DBTABLE_NAME + ".UserID=?;"
        );

        int col = 0;
        ps.setInt(++col, flatId);
        ps.setInt(++col, userId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Project_user t = null;
        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            t = I_ProjectUser.FillEntityFromExternal(rs);
        }

        return t;
    }

}
