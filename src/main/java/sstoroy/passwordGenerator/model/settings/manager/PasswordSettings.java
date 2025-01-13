package sstoroy.passwordGenerator.model.settings.manager;

import sstoroy.passwordGenerator.model.chance.Chance;
import sstoroy.passwordGenerator.model.settings.SettingKey;
import sstoroy.passwordGenerator.model.settings.value.SettingValue;

import java.util.Map;

/**
 * A class that represents the settings used for generating a password. The
 * 'set' methods mutates the class. Some settings must be set in the proper order,
 * for instance setting the alphabet should be done before setting no similar characters,
 * as that method changes the alphabet.
 */
public interface PasswordSettings {
    int passwordAmount();
    int passwordLength();
    String alphabet();
    String numbers();
    Chance numbersChance();
    String symbols();
    Chance symbolsChance();
    boolean onlyLowercase();
    boolean beginWithLetter();
    boolean noDuplicates();
    boolean excludeSimilarCharacters();

    SettingValue getSetting(SettingKey setting);

    PasswordSettings setSettings(Map<String, String> settingsMap);
    PasswordSettings setSetting(SettingKey setting, SettingValue value);

    /** Sets the amount of passwords to generate. If the amount is less than the minimum (1),
     * it will be set to the minimum, and vice versa for more than maximum (100). */
    PasswordSettings setPasswordAmount(int amount);

    /** Sets the password length. If the length is shorter than the minimum length,
     * it will be set to the min length, and vice versa for longer than maximum.
     * By default, the min length is 6 and max 128, but if the password is only
     * numbers, the min length is 1. */
    PasswordSettings setPasswordLength(int length);

    /** Sets the alphabet. Cleans dupes, numbers and special characters. If the given
     * alphabet is erroneous, sets it to the default. */
    PasswordSettings setAlphabet(String alphabet);

    /** Sets the number alphabet. Cleans dupes and special characters. If the given
     * number alphabet is erroneous, sets it to the default. */
    PasswordSettings setNumbers(String numbers);

    /** Sets the chance of getting numbers. If erroneous, sets it to default. If the
     * chance is always, overrules begin with letter */
    PasswordSettings setNumbersChance(Chance numbersChance);

    /** Sets the symbols alphabet. Cleans dupes, english letters and numbers. If the given
     *  alphabet is erroneous, sets it to the default. */
    PasswordSettings setSymbols(String specialCharacters);

    /** Sets the chance of getting symbols. If erroneous, sets it to default. If the
     * chance is always, overrules begin with letter */
    PasswordSettings setSymbolsChance(Chance specialCharacterChance);

    /** If true, sets the alphabet to only have lower characters. */
    PasswordSettings setOnlyLowercase(boolean onlyLowercase);

    /** If true, will assure that the password starts with a letter, as long as there
     * the chance of any letters are possible. If not, will set it to false. */
    PasswordSettings setBeginWithLetter(boolean beginWithLetter);

    /** If true, assures that there are no duplicate characters in the password. If it's
     * impossible due to the length of the password combined with the lengths of the
     * alphabet, will be set to false automatically. */
    PasswordSettings setNoDuplicates(boolean noDuplicates);

    /** If true, removes similar characters from all the alphabets. If a new alphabet
     * is set, it will remove similar characters from that alphabet. Setting this to
     * false after true will NOT reinstate the removed characters. */
    PasswordSettings setExcludeSimilarCharacters(boolean excludeSimilarCharacters);
}
