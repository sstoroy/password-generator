package sstoroy.passwordGenerator.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import sstoroy.passwordGenerator.model.generator.PasswordGenerator;
import sstoroy.passwordGenerator.model.generator.PasswordGeneratorImpl;
import sstoroy.passwordGenerator.model.settings.PasswordSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PasswordGeneratorAPIImpl implements PasswordGeneratorAPI {
    private final PasswordSettings settings;

    public PasswordGeneratorAPIImpl(PasswordSettings settings) {
        this.settings = settings;
    }

    private List<String> generatePasswords(int amount) {
        PasswordGenerator generator = new PasswordGeneratorImpl(settings);
        List<String> passwords = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            passwords.add(generator.generate());
        }
        return passwords;
    }

    @Override
    public ResponseEntity<List<String>> generateJSONWithPasswords(int amount) {
        List<String> passwords = generatePasswords(amount);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(passwords);
    }

    @Override
    public ResponseEntity<List<String>> generateJSONFromSettings() {

        return null;
    }

    @Override
    public ResponseEntity<Map<String, List<String>>> generateJSONWithPasswordsAndSettings(int amount) {
        return null;
    }
}
