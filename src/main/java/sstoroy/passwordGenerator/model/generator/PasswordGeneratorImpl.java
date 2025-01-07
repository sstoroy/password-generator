package sstoroy.passwordGenerator.model.generator;

import sstoroy.passwordGenerator.model.password.PasswordBuilder;
import sstoroy.passwordGenerator.model.password.PasswordBuilderImpl;
import sstoroy.passwordGenerator.model.settings.PasswordSettings;

import java.util.*;

/**
 * The main implementation of PasswordGenerator. Takes a PasswordSettings
 * to determine the parameters for the password.
 */
public class PasswordGeneratorImpl implements PasswordGenerator {
    private final Random random = new Random();
    private final PasswordSettings settings;

    public PasswordGeneratorImpl(PasswordSettings settings) {
        this.settings = settings;
    }

    @Override
    public String generate() {
        PasswordBuilder password = new PasswordBuilderImpl(
                settings.passwordLength(),
                settings.numbersChance(),
                settings.specialCharacterChance()
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
        List<Character> specialsPool = createPoolFromString(settings.specialCharacters());

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
