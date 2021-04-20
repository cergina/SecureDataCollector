/*
SONET SLOVAKIA - Secure Data Collector
@author: github.com/cergina
2020-2021
 */

package Model.Database.Support;

import java.sql.Date;
import java.sql.SQLException;

public class Assurance {
    // complex types

    // base types
    public static boolean isDateOk(Date value) {
        return (null != value);
    }

    public static boolean isIntOk(int value) {
        return true;
    }

    public static boolean isFkOk(int value) {
        return (value > 0);
    }

    public static boolean isVarcharOk(String value) {
        return (null != value);
    }

    public static void varcharCheck(String value) throws SQLException {
        if (isVarcharOk(value) == false)
            throw new SQLException("Value is not valid for use in database.");
    }

    public static void idCheck(int idToBeChecked) throws SQLException {
        if (isFkOk(idToBeChecked) == false)
            throw new SQLException("ID is less or equal to 0, which is an invalid Database id.");
    }
}
