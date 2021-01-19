package Database.Tables;

import java.util.ArrayList;

public class DbEntity {
    private static DbEntity reference = new DbEntity();
    public static String[] TABLE_CODENAMES = {
        "Reference", "Table", "CodeNames"
    };

    public ArrayList<String> GenerateHtmlTableRow_FromDbRow() {
        ArrayList arr = new ArrayList();

        return arr;
    }

    public String[] GetTableCodeNames() {
        String[] str = {};

        return str;
    }
}
