package sstoroy.passwordGenerator.model.settings.manager;

import sstoroy.passwordGenerator.model.chance.Chance;
import sstoroy.passwordGenerator.model.settings.SettingKey;
import sstoroy.passwordGenerator.model.settings.SettingValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PasswordSettingsImpl extends DefaultSettings {
    private final Map<SettingKey, SettingValue> settings = new HashMap<>(DefaultSettings.ALL);

    @Override public int passwordAmount() {return getSetting(SettingKey.AMOUNT_PASSWORDS).getInteger().orElseThrow();}
    @Override public int passwordLength() {return getSetting(SettingKey.PASSWORD_LENGTH).getInteger().orElseThrow();}
    @Override public String alphabet() {return getSetting(SettingKey.ALPHABET).getString().orElseThrow();}
    @Override public String numbers() {return getSetting(SettingKey.NUMBERS).getString().orElseThrow();}
    @Override public Chance numbersChance() {return getSetting(SettingKey.NUMBERS_CHANCE).getChance().orElseThrow();}
    @Override public String symbols() {return getSetting(SettingKey.SYMBOLS).getString().orElseThrow();}
    @Override public Chance symbolsChance() {return getSetting(SettingKey.SYMBOLS_CHANCE).getChance().orElseThrow();}
    @Override public boolean onlyLowercase() {return getSetting(SettingKey.ONLY_LOWERCASE).getBoolean().orElseThrow();}
    @Override public boolean beginWithLetter() {return getSetting(SettingKey.BEGIN_WITH_LETTER).getBoolean().orElseThrow();}
    @Override public boolean noDuplicates() {return getSetting(SettingKey.NO_DUPLICATES).getBoolean().orElseThrow();}
    @Override public boolean excludeSimilarCharacters() {return getSetting(SettingKey.EXCLUDE_SIMILAR).getBoolean().orElseThrow();}

    @Override
    public SettingValue getSetting(SettingKey setting) {
        return settings.get(setting);
    }

    @Override
    public PasswordSettings setSettings(Map<String, String> settingsMap) {
        Map<SettingKey, SettingValue> paramValues = cleanMap(settingsMap);

        for (SettingKey maybeMissing : SettingKey.values()) {
            if (!paramValues.containsKey(maybeMissing)) {
                paramValues.put(maybeMissing, getDefaultSetting(maybeMissing));
            }
        }
        for (SettingKey key : SettingKey.inOrder()) {
            setSetting(key, paramValues.get(key));
        }

        return this;
    }

    private Map<SettingKey, SettingValue> cleanMap(Map<String, String> stringMap) {
        Map<SettingKey, SettingValue> cleaned = new HashMap<>(stringMap.size());
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            Optional<SettingKey> key = SettingKey.fromParamName(entry.getKey());
            if (key.isEmpty()) continue;

            Optional<SettingValue> value = SettingValue.ofUnknown(key.get(), entry.getValue());
            if (value.isEmpty()) continue;

            cleaned.put(key.get(), value.get());
        }
        return cleaned;
    }

    @Override
    public PasswordSettings setSetting(SettingKey setting, SettingValue value) {
        switch (setting) {
            case AMOUNT_PASSWORDS -> {
                Optional<Integer> maybeValue = value.getInteger();
                if (maybeValue.isEmpty()) break;
                setPasswordAmount(maybeValue.get());
            }
            case PASSWORD_LENGTH -> {
                Optional<Integer> maybeValue = value.getInteger();
                if (maybeValue.isEmpty()) break;
                setPasswordLength(maybeValue.get());
            }
            case ALPHABET -> {
                Optional<String> maybeValue = value.getString();
                if (maybeValue.isEmpty()) break;
                setAlphabet(maybeValue.get());
            }
            case NUMBERS -> {
                Optional<String> maybeValue = value.getString();
                if (maybeValue.isEmpty()) break;
                setNumbers(maybeValue.get());
            }
            case NUMBERS_CHANCE -> {
                Optional<Chance> maybeValue = value.getChance();
                if (maybeValue.isEmpty()) break;
                setNumbersChance(maybeValue.get());
            }
            case SYMBOLS -> {
                Optional<String> maybeValue = value.getString();
                if (maybeValue.isEmpty()) break;
                setSymbols(maybeValue.get());
            }
            case SYMBOLS_CHANCE -> {
                Optional<Chance> maybeValue = value.getChance();
                if (maybeValue.isEmpty()) break;
                setSymbolsChance(maybeValue.get());
            }
            case ONLY_LOWERCASE -> {
                Optional<Boolean> maybeValue = value.getBoolean();
                if (maybeValue.isEmpty()) break;
                setOnlyLowercase(maybeValue.get());
            }
            case BEGIN_WITH_LETTER -> {
                Optional<Boolean> maybeValue = value.getBoolean();
                if (maybeValue.isEmpty()) break;
                setBeginWithLetter(maybeValue.get());
            }
            case NO_DUPLICATES -> {
                Optional<Boolean> maybeValue = value.getBoolean();
                if (maybeValue.isEmpty()) break;
                setNoDuplicates(maybeValue.get());
            }
            case EXCLUDE_SIMILAR -> {
                Optional<Boolean> maybeValue = value.getBoolean();
                if (maybeValue.isEmpty()) break;
                setExcludeSimilarCharacters(maybeValue.get());
            }
            default -> {}
        }
        return this;
    }

    @Override
    public PasswordSettings setPasswordAmount(int amount) {
        if (amount < MIN_PASSWORD_AMOUNT) amount = MIN_PASSWORD_AMOUNT;
        else if (amount > MAX_PASSWORD_AMOUNT) amount = MAX_PASSWORD_AMOUNT;
        settings.put(SettingKey.AMOUNT_PASSWORDS, SettingValue.of(amount));
        return this;
    }

    @Override
    public PasswordSettings setPasswordLength(int length) {
        if (length < MIN_LENGTH && !numbersChance().isAlways()) {
            length = MIN_LENGTH;
        } else if (length < MIN_LENGTH_NO && numbersChance().isAlways()) {
            length = MIN_LENGTH_NO;
        } else if (length > MAX_LENGTH) {
                length = MAX_LENGTH;
        }

        settings.put(SettingKey.PASSWORD_LENGTH, SettingValue.of(length));
        // make sure that the length allows no dupes
        setNoDuplicates(noDuplicates());
        return this;
    }

    @Override
    public PasswordSettings setAlphabet(String alphabet) {
        if (alphabet == null || alphabet.isEmpty() || alphabet.length() > MAX_LENGTH) {
            alphabet = getDefaultSetting(SettingKey.ALPHABET).getStringUnsafe();
        }
        StringBuilder alphabetBuilder = new StringBuilder();
        for (char c : alphabet.toCharArray()) {
            // no dupes in current alphabet, not an integer and not currently special character
            if (
                doesNotContain(alphabetBuilder.toString(), c)
                && doesNotContain(getDefaultSetting(SettingKey.NUMBERS).getStringUnsafe(), c)
                && doesNotContain(getDefaultSetting(SettingKey.SYMBOLS).getStringUnsafe(), c)
                && !(excludeSimilarCharacters() && SIMILAR_CHARACTERS.contains(c))
            ) {
                alphabetBuilder.append(c);
            }
        }
        if (alphabetBuilder.isEmpty()) {
            alphabet = getDefaultSetting(SettingKey.ALPHABET).getStringUnsafe();
        } else {
            alphabet = alphabetBuilder.toString();
        }
        settings.put(SettingKey.ALPHABET, SettingValue.of(alphabet));
        return this;
    }

    private boolean doesNotContain(String pool, char c) {
        return !pool.contains(String.valueOf(c));
    }

    @Override
    public PasswordSettings setNumbers(String numbers) {
        if (numbers == null || numbers.isEmpty() || numbers.length() > MAX_LENGTH) {
            numbers = DefaultSettings.getDefaultSetting(SettingKey.NUMBERS).getStringUnsafe();
        }
        StringBuilder numbersBuilder = new StringBuilder();
        for (Character c : numbers.toCharArray()) {
            // no dupes, not a default character and is an integer
            if (
                doesNotContain(numbersBuilder.toString(), c)
                && doesNotContain(getDefaultSetting(SettingKey.ALPHABET).getStringUnsafe(), c)
                && doesNotContain(getDefaultSetting(SettingKey.NUMBERS).getStringUnsafe(), c)
                && !(excludeSimilarCharacters() && SIMILAR_CHARACTERS.contains(c))
            ) {
                numbersBuilder.append(c);
            }
        }
        if (numbersBuilder.isEmpty()) {
            numbers = DefaultSettings.getDefaultSetting(SettingKey.NUMBERS).getStringUnsafe();
        } else {
            numbers = numbersBuilder.toString();
        }
        settings.put(SettingKey.NUMBERS, SettingValue.of(numbers));
        return this;
    }

    @Override
    public PasswordSettings setNumbersChance(Chance numbersChance) {
        if (numbersChance == null) {
            numbersChance = getDefaultSetting(SettingKey.NUMBERS_CHANCE).getChanceUnsafe();
        }
        if (numbersChance.isAlways()) {
            setBeginWithLetter(false);
        }
        settings.put(SettingKey.NUMBERS_CHANCE, SettingValue.of(numbersChance));
        return this;
    }

    @Override
    public PasswordSettings setSymbols(String symbols) {
        if (symbols == null || symbols.isEmpty() || symbols.length() > MAX_LENGTH) {
            symbols = getDefaultSetting(SettingKey.SYMBOLS).getStringUnsafe();
        }
        settings.put(SettingKey.SYMBOLS, SettingValue.of(symbols));
        return this;
    }

    @Override
    public PasswordSettings setSymbolsChance(Chance symbols) {
        if (symbols == null) {
            symbols = getDefaultSetting(SettingKey.SYMBOLS_CHANCE).getChanceUnsafe();
        }
        if (symbols.isAlways()) {
            setBeginWithLetter(false);
        }
        settings.put(SettingKey.SYMBOLS_CHANCE, SettingValue.of(symbols));
        return this;
    }

    @Override
    public PasswordSettings setOnlyLowercase(boolean onlyLowercase) {
        settings.put(SettingKey.ONLY_LOWERCASE, SettingValue.of(onlyLowercase));
        return this;
    }

    @Override
    public PasswordSettings setExcludeSimilarCharacters(boolean excludeSimilarCharacters) {
        if (!excludeSimilarCharacters) {
            settings.put(SettingKey.EXCLUDE_SIMILAR, SettingValue.of(false));
            return this;
        }
        String pool = removeSimilarCharacters(alphabet());
        if (pool.isEmpty()) {
            pool = removeSimilarCharacters(getDefaultSetting(SettingKey.ALPHABET).getStringUnsafe());
        }
        setAlphabet(pool);

        pool = removeSimilarCharacters(numbers());
        if (pool.isEmpty()) {
            pool = removeSimilarCharacters(getDefaultSetting(SettingKey.ALPHABET).getStringUnsafe());
        }
        setNumbers(pool);

        pool = removeSimilarCharacters(symbols());
        if (pool.isEmpty()) {
            pool = removeSimilarCharacters(getDefaultSetting(SettingKey.SYMBOLS).getStringUnsafe());
        }
        setSymbols(pool);
        settings.put(SettingKey.EXCLUDE_SIMILAR, SettingValue.of(true));
        return this;
    }

    private String removeSimilarCharacters(String originalPool) {
        StringBuilder poolBuilder = new StringBuilder();
        for (Character c : originalPool.toCharArray()) {
            if (!SIMILAR_CHARACTERS.contains(c)) {
                poolBuilder.append(c);
            }
        }
        return poolBuilder.toString();
    }

    @Override
    public PasswordSettings setBeginWithLetter(boolean beginWithLetter) {
        beginWithLetter = beginWithLetter
                && (numbersChance().chancePercentage()
                + symbolsChance().chancePercentage()
                < 100
        );
        settings.put(SettingKey.BEGIN_WITH_LETTER, SettingValue.of(beginWithLetter));
        return this;
    }

    @Override
    public PasswordSettings setNoDuplicates(boolean noDuplicates) {
        if (!noDuplicates) {
            settings.put(SettingKey.NO_DUPLICATES, SettingValue.of(false));
            return this;
        }
        // check that it's possible to create a password with no dupes
        boolean enoughLetters =
                Chance.opposite(numbersChance())
                        .getPercentageAmount(passwordLength())
                        <= alphabet().length();
        boolean enoughNumbers = numbersChance()
                .getPercentageAmount(passwordLength())
                <= numbers().length();
        boolean enoughSpecials = symbolsChance()
                .getPercentageAmount(passwordLength())
                <= symbols().length();
        noDuplicates = enoughLetters && enoughNumbers && enoughSpecials;
        settings.put(SettingKey.NO_DUPLICATES, SettingValue.of(noDuplicates));
        return this;
    }
}
