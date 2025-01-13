package sstoroy.passwordGenerator.model.generator;

import org.springframework.web.util.UriComponentsBuilder;
import sstoroy.passwordGenerator.model.password.PasswordBuilder;
import sstoroy.passwordGenerator.model.password.PasswordBuilderImpl;
import sstoroy.passwordGenerator.model.settings.SettingKey;
import sstoroy.passwordGenerator.model.settings.manager.PasswordSettings;
import sstoroy.passwordGenerator.view.PasswordGeneratorResponse;

import java.util.*;

/**
 * The main implementation of PasswordGenerator. Takes a PasswordSettings
 * to determine the parameters for the password.
 */
public class PasswordGeneratorImpl implements PasswordGenerator, PasswordGeneratorResponse {
    private final Random random = new Random();
    private final PasswordSettings settings;

    public PasswordGeneratorImpl(PasswordSettings settings) {
        this.settings = settings;
    }

    private List<String> generatePasswords() {
        int amount = settings.getSetting(SettingKey.AMOUNT_PASSWORDS).getIntegerUnsafe();
        List<String> passwords = new ArrayList<>(amount);
        for (int i=0;i<amount;i++) {
            passwords.add(generate());
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

    @Override
    public String generate() {
        PasswordBuilder password = new PasswordBuilderImpl(
                settings.passwordLength(),
                settings.numbersChance(),
                settings.symbolsChance()
        );

        // character pools for the password creation
        // if noDupes is enabled, will lose characters as the password is populated
        List<Character> lettersPool = createPoolFromString(settings.alphabet());
        if (!settings.onlyLowercase()) {
            int poolSize = lettersPool.size();
            for (int i=0;i<poolSize;i++) {
                char letter = Character.toUpperCase(lettersPool.get(i));
                lettersPool.add(letter);
            }
        }
        List<Character> numbersPool = createPoolFromString(settings.numbers());
        List<Character> specialsPool = createPoolFromString(settings.symbols());

        populatePasswordFromPool(password, password.letterIndexes(), lettersPool);
        populatePasswordFromPool(password, password.numberIndexes(), numbersPool);
        populatePasswordFromPool(password, password.specialIndexes(), specialsPool);
        tryAssignFirstLetter(password, lettersPool);

        return password.toString();
    }

    private static List<Character> createPoolFromString(String stringPool) {
        List<Character> pool = new ArrayList<>(stringPool.length());
        for (int i = 0; i < stringPool.length(); i++) {
            pool.add(stringPool.charAt(i));
        }
        return pool;
    }

    private void populatePasswordFromPool(PasswordBuilder password, Iterable<Integer> indexes, List<Character> pool) {
        for (Integer index: indexes) {
            set(password, index, pool);
        }
    }

    private void tryAssignFirstLetter(PasswordBuilder password, List<Character> pool) {
        if (settings.beginWithLetter()) {
            set(password, 0, pool);
        }
    }

    /**
     * Set a character from the pool in the given index at the password.
     * @param password the password to edit
     * @param index the index of the password to set
     * @param pool the pool from which the character to set are gotten from
     */
    private void set(PasswordBuilder password, int index, List<Character> pool) {
        if (pool.isEmpty()) {
            throw new IllegalArgumentException("Pool" + pool + " is empty");
        }
        int randomIndex = random.nextInt(pool.size());
        char toSet = pool.get(randomIndex);
        if (settings.noDuplicates()) {
            pool.remove(randomIndex);
        }
        password.set(index, toSet);
    }
}
