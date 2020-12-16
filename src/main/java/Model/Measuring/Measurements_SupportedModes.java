package Model.Measuring;

public enum Measurements_SupportedModes {
    REGULAR_MESSAGE("measurements"),
    ERROR_MESSAGE("errors");

    public final String label;

    private Measurements_SupportedModes(String label) {
        this.label = label;
    }

    // Getter like
    public static Measurements_SupportedModes valueOfLabel(String label) {
        for (Measurements_SupportedModes e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
