package sstoroy.passwordGenerator.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface PasswordGeneratorAPI {
    String SETTINGS_PASSWORD_LENGTH = "length";
    String SETTINGS_ALPHABET = "alphabet";
    String SETTINGS_NUMBERS = "numbers";
    String SETTINGS_NUMBERS_CHANCE = "numberChance";
    String SETTINGS_SPECIAL_CHARACTERS = "special";
    String SETTINGS_SPECIAL_CHANCE = "specialCharactersChance";
    String SETTINGS_ONLY_LOWERCASE = "only_lowercase";
    String SETTINGS_BEGIN_WITH_LETTER = "begin_with_letter";
    String SETTINGS_NO_DUPLICATES = "noDuplicates";
    String SETTINGS_EXCLUDE_SIMILAR = "exclude_similar";

    ResponseEntity<List<String>> generateJSONWithPasswords(int amount);
    ResponseEntity<Map<String, String>> generateJSONFromSettings();
    <T> ResponseEntity<Map<String, T>> generateJSONWithPasswordsAndSettings(int amount);
}
