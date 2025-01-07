package sstoroy.passwordGenerator.model.settings;

import java.util.Set;

public abstract class DefaultSettings implements PasswordSettings {
    public final static int MIN_LENGTH_NO = 1;
    public final static int MIN_LENGTH = 6;
    public final static int MAX_LENGTH = 128;
    public final static Set<Character> SIMILAR_CHARACTERS = Set.of('0', '1', 'o', 'i', 'l', 'q', 'O', 'I', 'Q');

    // default settings as strings
    // implementations can parse these as they like, or not
    public final static String PASSWORD_LENGTH = "10";
    public final static String ENGLISH_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public final static String DECIMAL_NUMBERS = "0123456789";
    public final static String NUMBERS_CHANCE = "25";
    public final static String SPECIAL_CHARACTERS = "!\"#$%&'()*+,-./:;<=>?@[]^_`{|}~\\ ";
    public final static String SPECIAL_CHARACTER_CHANCE_PERCENTAGE = "25";
    public final static String ONLY_LOWERCASE = "false";
    public final static String BEGIN_WITH_LETTER = "true";
    public final static String NO_DUPLICATES = "false";
    public final static String EXCLUDE_SIMILAR_CHARACTERS = "false";

}
