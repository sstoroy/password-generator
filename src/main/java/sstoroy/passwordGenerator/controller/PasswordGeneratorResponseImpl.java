package sstoroy.passwordGenerator.controller;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import sstoroy.passwordGenerator.model.generator.PasswordGenerator;
import sstoroy.passwordGenerator.model.generator.PasswordGeneratorImpl;
import sstoroy.passwordGenerator.model.settings.PasswordSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordGeneratorResponseImpl implements PasswordGeneratorResponse {
    private final int amount;
    private final PasswordSettings settings;

    public PasswordGeneratorResponseImpl(int amount, PasswordSettings settings) {
        this.amount = amount;
        this.settings = settings;
    }

    private List<String> generatePasswords() {
        PasswordGenerator generator = new PasswordGeneratorImpl(settings);
        List<String> passwords = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            passwords.add(generator.generate());
        }
        return passwords;
    }

    @Override
    public List<String> getPasswords() {
        return generatePasswords();
    }

    @Override
    public Map<String, String> getSettings() {
        Map<String, String> settingsAsStrings = new HashMap<>();
        settingsAsStrings.put(PasswordGeneratorResponse.SETTINGS_AMOUNT_PASSWORDS, String.valueOf(amount));
        settingsAsStrings.put(PasswordGeneratorResponse.SETTINGS_ALPHABET, settings.alphabet());
        settingsAsStrings.put(PasswordGeneratorResponse.SETTINGS_NUMBERS, settings.numbers());
        settingsAsStrings.put(PasswordGeneratorResponse.SETTINGS_NUMBERS_CHANCE, settings.numbersChance().toString());
        settingsAsStrings.put(PasswordGeneratorResponse.SETTINGS_SPECIAL_CHARACTERS, settings.specialCharacters());
        settingsAsStrings.put(PasswordGeneratorResponse.SETTINGS_SPECIAL_CHANCE, settings.specialCharacterChance().toString());
        settingsAsStrings.put(PasswordGeneratorResponse.SETTINGS_ONLY_LOWERCASE, String.valueOf(settings.onlyLowercase()));
        settingsAsStrings.put(PasswordGeneratorResponse.SETTINGS_BEGIN_WITH_LETTER, String.valueOf(settings.beginWithLetter()));
        settingsAsStrings.put(PasswordGeneratorResponse.SETTINGS_NO_DUPLICATES, String.valueOf(settings.noDuplicates()));
        settingsAsStrings.put(PasswordGeneratorResponse.SETTINGS_EXCLUDE_SIMILAR, String.valueOf(settings.excludeSimilarCharacters()));
        return settingsAsStrings;
    }

    @Override
    public String getParameterURL() {
        Map<String, String> parameters = getSettings();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("");
        for (String key : parameters.keySet()) {
            uriBuilder.queryParam(key, parameters.get(key));
        }
        return uriBuilder.build().encode().toUriString();
    }
}
