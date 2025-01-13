package sstoroy.passwordGenerator.model.settings.manager;

import sstoroy.passwordGenerator.model.chance.Chance;
import sstoroy.passwordGenerator.model.settings.SettingKey;
import sstoroy.passwordGenerator.model.settings.value.SettingValue;

import java.util.Map;
import java.util.Set;

public abstract class DefaultSettings implements PasswordSettings {
    public final static int MIN_LENGTH_NO = 1;
    public final static int MIN_LENGTH = 6;
    public final static int MAX_LENGTH = 128;
    public final static int MIN_PASSWORD_AMOUNT = 1;
    public final static int MAX_PASSWORD_AMOUNT = 50;
    public final static Set<Character> SIMILAR_CHARACTERS = Set.of(
            '0', '1', 'o', 'i', 'l', 'q', 'O', 'I', 'Q'
    );

    /** A map of all the default settings. Each value of SettingKey must have a corresponding value. */
    protected final static Map<SettingKey, SettingValue> ALL = Map.ofEntries(
                Map.entry(SettingKey.AMOUNT_PASSWORDS, SettingValue.of(1)),
                Map.entry(SettingKey.PASSWORD_LENGTH, SettingValue.of(8)),
                Map.entry(SettingKey.ONLY_LOWERCASE, SettingValue.of(false)),
                Map.entry(SettingKey.BEGIN_WITH_LETTER, SettingValue.of(true)),
                Map.entry(SettingKey.NO_DUPLICATES, SettingValue.of(false)),
                Map.entry(SettingKey.EXCLUDE_SIMILAR, SettingValue.of(false)),
                Map.entry(SettingKey.LETTERS, SettingValue.of("abcdefghijklmnopqrstuvwxyz")),
                Map.entry(SettingKey.NUMBERS, SettingValue.of("0123456789")),
                Map.entry(SettingKey.SYMBOLS, SettingValue.of("! #$%&'()*+,-./\\:;<>=?@[]^_`{|}~")),
                Map.entry(SettingKey.NUMBERS_CHANCE, SettingValue.of(Chance.of(25))),
                Map.entry(SettingKey.SYMBOLS_CHANCE, SettingValue.of(Chance.of(25)))
        );

    public static SettingValue getDefaultSetting(SettingKey setting) {
        return ALL.get(setting);
    }
}
