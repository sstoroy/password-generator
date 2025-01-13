package sstoroy.passwordGenerator.model.settings.value;

import sstoroy.passwordGenerator.model.chance.Chance;
import sstoroy.passwordGenerator.model.settings.SettingKey;

import java.util.Optional;

import static sstoroy.passwordGenerator.model.settings.SettingType.*;
import static sstoroy.passwordGenerator.model.settings.SettingType.CHANCE;

/**
 * Represents the value of the setting. Can be either an Integer,
 * Boolean, String or Chance. Use the corresponding of() method to
 * create a new SettingValue, or use ofUnknown() with the correct
 * SettingKey if the type is unknown.
 */
public interface SettingValue {
    /** @return an Optional with the Integer if possible, otherwise Optional.empty() */
    Optional<Integer> getInteger();

    /** @return an Optional with the String if possible, otherwise Optional.empty() */
    Optional<String> getString();

    /** @return an Optional with the Boolean if possible, otherwise Optional.empty() */
    Optional<Boolean> getBoolean();

    /** @return an Optional with the Chance if possible, otherwise Optional.empty() */
    Optional<Chance> getChance();

    /** Only use if you're absolutely sure the value is an Integer.
     * @return an Integer, or throws cast exception. */
    Integer getIntegerUnsafe();

    /** Only use if you're absolutely sure the value is a String.
     * @return a String, or throws cast exception. */
    String getStringUnsafe();

    /** Only use if you're absolutely sure the value is a Boolean.
     * @return a Boolean, or throws cast exception. */
    Boolean getBooleanUnsafe();

    /** Only use if you're absolutely sure the value is a Chance.
     * @return a Chance, or throws cast exception. */
    Chance getChanceUnsafe();

    /** @return a String value of the stored value */
    String valueString();

    /** Creates a new SettingValue with the given value */
    static SettingValue of(Integer value) {
        return new SettingValueImpl(value, INT);
    }

    /** Creates a new SettingValue with the given value */
    static SettingValue of(String value) {
        return new SettingValueImpl(value, CHARS);
    }

    /** Creates a new SettingValue with the given value */
    static SettingValue of(Boolean value) {
        return new SettingValueImpl(value, BOOL);
    }

    /** Creates a new SettingValue with the given value */
    static SettingValue of(Chance value) {
        return new SettingValueImpl(value, CHANCE);
    }

    /** Creates a new SettingValue from the given value */
    static SettingValue ofInteger(String value) {
        return of(Integer.parseInt(value));
    }

    /** Creates a new SettingValue from the given value */
    static SettingValue ofBoolean(String value) {
        return of(Boolean.parseBoolean(value));
    }

    /** Creates a new SettingValue from the given value */
    static SettingValue ofChance(String value) {
        return of(Chance.of(value));
    }

    /** Creates a new SettingValue from the given value and SettingKey.
     * The value type is dependent on the key. If it's impossible to
     * create a SettingValue from the parameters, returns Optional.empty() */
    static Optional<SettingValue> ofUnknown(SettingKey forKey, String value) {
        try {
            return switch (forKey.getType()) {
                case BOOL -> Optional.of(ofBoolean(value));
                case INT -> Optional.of(ofInteger(value));
                case CHARS -> Optional.of(of(value));
                case CHANCE -> Optional.of(ofChance(value));
            };
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
