package Model.Database.Tables;

import Model.Database.Support.CustomLogs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbEntity {
    private static DbEntity reference = new DbEntity();
    public static String[] TABLE_CODENAMES = {
        "Reference", "Table", "CodeNames"
    };

    public List<String> GenerateHtmlTableRow_FromDbRow() {
        List<String> arr = new ArrayList<String>();

        return arr;
    }

    public String[] GetTableCodeNames() {
        String[] str = {};

        return str;
    }

    public String GetDbTableName() {
        return "DbEntity non valid in database";
    }

    public DbEntity FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return new DbEntity();
    }

    public static <T extends DbEntity> T ReturnUnusable(Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (Exception e) {
            CustomLogs.Error("Cannot instantiate " + tClass.getName());
        }
        return null;
    }
}
