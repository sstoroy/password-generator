package sstoroy.passwordGenerator.model.settings.value;

import sstoroy.passwordGenerator.model.chance.Chance;
import sstoroy.passwordGenerator.model.settings.SettingType;

import java.util.Optional;

import static sstoroy.passwordGenerator.model.settings.SettingType.*;

record SettingValueImpl(Object value, SettingType type) implements SettingValue {
    @Override
    public Optional<Integer> getInteger() {
        if (type != INT) return Optional.empty();
        return Optional.of(getIntegerUnsafe());
    }

    @Override
    public Optional<String> getString() {
        if (type != CHARS) return Optional.empty();
        return Optional.of(getStringUnsafe());
    }

    @Override
    public Optional<Boolean> getBoolean() {
        if (type != BOOL) return Optional.empty();
        return Optional.of(getBooleanUnsafe());
    }

    @Override
    public Optional<Chance> getChance() {
        if (type != CHANCE) return Optional.empty();
        return Optional.of(getChanceUnsafe());
    }

    @Override
    public Integer getIntegerUnsafe() {
        return (Integer) value;
    }

    @Override
    public String getStringUnsafe() {
        return (String) value;
    }

    @Override
    public Boolean getBooleanUnsafe() {
        return (Boolean) value;
    }

    @Override
    public Chance getChanceUnsafe() {
        return (Chance) value;
    }

    @Override
    public String valueString() {
        return value.toString();
    }
}
