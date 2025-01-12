package sstoroy.passwordGenerator.controller;

import org.springframework.web.util.UriComponentsBuilder;
import sstoroy.passwordGenerator.model.generator.PasswordGenerator;
import sstoroy.passwordGenerator.model.generator.PasswordGeneratorImpl;
import sstoroy.passwordGenerator.model.settings.manager.PasswordSettings;
import sstoroy.passwordGenerator.model.settings.SettingKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordGeneratorResponseImpl implements PasswordGeneratorResponse {
    private final PasswordSettings settings;

    public PasswordGeneratorResponseImpl(PasswordSettings settings) {
        this.settings = settings;
    }

    private List<String> generatePasswords() {
        PasswordGenerator generator = new PasswordGeneratorImpl(settings);
        int amount = settings.getSetting(SettingKey.AMOUNT_PASSWORDS).getIntegerUnsafe();
        List<String> passwords = new ArrayList<>(amount);
        for (int i=0;i<amount;i++) {
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
        for (SettingKey key : SettingKey.values()) {
            settingsAsStrings.put(key.getParam(), settings.getSetting(key).valueString());
        }
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
