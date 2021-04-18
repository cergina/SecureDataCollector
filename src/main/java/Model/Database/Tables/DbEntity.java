package Model.Database.Tables;

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
}
