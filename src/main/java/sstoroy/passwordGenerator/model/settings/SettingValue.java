package sstoroy.passwordGenerator.model.settings;

import sstoroy.passwordGenerator.model.chance.Chance;

import java.util.Objects;
import java.util.Optional;

import static sstoroy.passwordGenerator.model.settings.SettingType.*;

public class SettingValue {
    private final Object value;
    private final SettingType type;

    private SettingValue(Object value, SettingType type) {
        this.value = value;
        this.type = type;
    }

    public static SettingValue of(Integer value) {
        return new SettingValue(value, INT);
    }

    public static SettingValue of(String value) {
        return new SettingValue(value, CHARS);
    }

    public static SettingValue of(Boolean value) {
        return new SettingValue(value, BOOL);
    }

    public static SettingValue of(Chance value) {
        return new SettingValue(value, CHANCE);
    }

    public static SettingValue ofInteger(String value) {
        return of(Integer.parseInt(value));
    }

    public static SettingValue ofBoolean(String value) {
        return of(Boolean.parseBoolean(value));
    }

    public static SettingValue ofChance(String value) {
        return of(Chance.of(value));
    }

    public static Optional<SettingValue> ofUnknown(SettingKey forKey, String value) {
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

    public Optional<Integer> getInteger() {
        if (type != INT) return Optional.empty();
        return Optional.of(getIntegerUnsafe());
    }

    public Optional<String> getString() {
        if (type != CHARS) return Optional.empty();
        return Optional.of(getStringUnsafe());
    }

    public Optional<Boolean> getBoolean() {
        if (type != BOOL) return Optional.empty();
        return Optional.of(getBooleanUnsafe());
    }

    public Optional<Chance> getChance() {
        if (type != CHANCE) return Optional.empty();
        return Optional.of(getChanceUnsafe());
    }

    public Integer getIntegerUnsafe() {
        return (Integer) value;
    }

    public String getStringUnsafe() {
        return (String) value;
    }

    public Boolean getBooleanUnsafe() {
        return (Boolean) value;
    }

    public Chance getChanceUnsafe() {
        return (Chance) value;
    }

    public String valueString() {
        return value.toString();
    }

    @Override
    public String toString() {
        return "SettingValue[" + value + "]";
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof SettingValue that)) return false;

        return Objects.equals(value, that.value) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(value);
        result = 31 * result + Objects.hashCode(type);
        return result;
    }
}
