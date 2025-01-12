package sstoroy.passwordGenerator.model.settings;

public enum SettingType {
    BOOL("checkbox"),
    INT("number"),
    CHARS("text"),
    CHANCE("range"),
    ;

    private final String inputType;

    SettingType(String inputType) {
        this.inputType = inputType;
    }

    public String getInputType() {
        return inputType;
    }
}
