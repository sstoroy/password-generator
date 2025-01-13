package sstoroy.passwordGenerator.model.settings;

import java.util.List;
import java.util.Optional;

import static sstoroy.passwordGenerator.model.settings.SettingType.*;

public enum SettingKey {
    AMOUNT_PASSWORDS(10, "Number of passwords", "amount", INT),
    PASSWORD_LENGTH(9, "Password length", "length", INT),
    LETTERS(0, "Letters", "letters", CHARS),
    NUMBERS(1, "Numbers", "numbers", CHARS),
    NUMBERS_CHANCE(3, "Number to letter ratio", "number_chance", CHANCE),
    SYMBOLS(2, "Symbols", "symbols", CHARS),
    SYMBOLS_CHANCE(4, "Symbol to other characters ratio", "symbol_chance", CHANCE),
    ONLY_LOWERCASE(5, "Only lowercase letters", "only_lowercase", BOOL),
    BEGIN_WITH_LETTER(6, "Password begins with letter", "begin_with_letter", BOOL),
    NO_DUPLICATES(7, "No duplicate characters", "no_duplicates", BOOL),
    EXCLUDE_SIMILAR(8, "Exclude similar characters", "exclude_similar", BOOL),
    ;

    private final String displayName;
    private final String paramName;
    private final SettingType type;
    private final int order;
    
    SettingKey(int order, String displayName, String paramName, SettingType type) {
        this.order = order;
        this.displayName = displayName;
        this.paramName = paramName;
        this.type = type;
    }

    /** @return the key as it is shown to the public */
    public String getDisplay() {
        return displayName;
    }

    /** @return the key as it is represented as a parameter (for instance in URIs) */
    public String getParam() {
        return paramName;
    }

    /** @return the type of key */
    public SettingType getType() {
        return type;
    }

    public static Iterable<SettingKey> inOrder() {
        SettingKey[] keys = new SettingKey[values().length];
        for (SettingKey key : values()) {
            keys[key.order] = key;
        }
        return List.of(keys);
    }

    /**
     * Tries to find a corresponding key to its parameter name. Also works as
     * a safer option to valueOf().
     * @param paramName the parameter name, or the String representation of the key
     * @return an Optional with the key
     */
    public static Optional<SettingKey> fromParamName(String paramName) {
        for (SettingKey key : values()) {
            if (key.paramName.equals(paramName) || key.toString().equals(paramName)) {
                return Optional.of(key);
            }
        }
        return Optional.empty();
    }
}
