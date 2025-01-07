package sstoroy.passwordGenerator.model.settings;

import sstoroy.passwordGenerator.model.chance.Chance;

public class PasswordSettingsImpl extends DefaultSettings {
    private int length = DefaultSettings.MIN_LENGTH;
    private String alphabet = ENGLISH_ALPHABET;
    private String numbers = DECIMAL_NUMBERS;
    private Chance numberChance = Chance.of(NUMBERS_CHANCE);
    private String specialCharacters = SPECIAL_CHARACTERS;
    private Chance specialCharacterChance = Chance.of(SPECIAL_CHARACTER_CHANCE_PERCENTAGE);
    private boolean onlyLowercase = Boolean.parseBoolean(ONLY_LOWERCASE);
    private boolean beginWithLetter = Boolean.parseBoolean(BEGIN_WITH_LETTER);
    private boolean noDuplicates = Boolean.parseBoolean(NO_DUPLICATES);

    @Override public int passwordLength() {return length;}
    @Override public String alphabet() {return alphabet;}
    @Override public String numbers() {return numbers;}
    @Override public Chance numbersChance() {return numberChance;}
    @Override public String specialCharacters() {return specialCharacters;}
    @Override public Chance specialCharacterChance() {return specialCharacterChance;}
    @Override public boolean onlyLowercase() {return onlyLowercase;}
    @Override public boolean beginWithLetter() {return beginWithLetter;}
    @Override public boolean noDuplicates() {return noDuplicates;}

    @Override
    public PasswordSettings setPasswordLength(int length) {
        if (length < MIN_LENGTH && !numbersChance().isAlways()) {
            this.length = MIN_LENGTH;
        } else if (length < MIN_LENGTH_NO && numbersChance().isAlways()) {
            this.length = MIN_LENGTH_NO;
        } else if (length > MAX_LENGTH) {
                this.length = MAX_LENGTH;
        } else {
            this.length = length;
        }
        // make sure that the length allows no dupes
        setNoDuplicates(this.noDuplicates);
        return this;
    }

    @Override
    public PasswordSettings setAlphabet(String alphabet) {
        if (alphabet == null || alphabet.isEmpty()) {
            alphabet = ENGLISH_ALPHABET;
        } else {
            StringBuilder alphabetBuilder = new StringBuilder();
            for (char c : alphabet.toCharArray()) {
                // no dupes in current alphabet, not an integer and not currently special character
                if (
                    doesNotContain(alphabetBuilder.toString(), c) &&
                    doesNotContain(DefaultSettings.DECIMAL_NUMBERS, c) &&
                    doesNotContain(DefaultSettings.SPECIAL_CHARACTERS, c)
                ) {
                    alphabetBuilder.append(c);
                }
                if (alphabetBuilder.isEmpty()) {
                    alphabet = ENGLISH_ALPHABET;
                } else {
                    alphabet = alphabetBuilder.toString();
                }
            }
        }
        this.alphabet = alphabet;
        return this;
    }

    private boolean doesNotContain(String pool, char c) {
        return !pool.contains(String.valueOf(c));
    }

    @Override
    public PasswordSettings setNumbers(String numbers) {
        if (numbers == null || numbers.isEmpty()) {
            numbers = DECIMAL_NUMBERS;
        } else {
            StringBuilder numbersBuilder = new StringBuilder();
            for (Character c : numbers.toCharArray()) {
                // no dupes, and is an integer
                if (
                    doesNotContain(numbersBuilder.toString(), c) &&
                    DefaultSettings.DECIMAL_NUMBERS.contains(String.valueOf(c))
                ) {
                    numbersBuilder.append(c);
                }
                if (numbersBuilder.isEmpty()) {
                    numbers = DECIMAL_NUMBERS;
                } else {
                    numbers = numbersBuilder.toString();
                }
            }
        }
        this.numbers = numbers;
        return this;
    }

    @Override
    public PasswordSettings setNumbersChance(Chance numbersChance) {
        if (numbersChance == null) {
            numbersChance = Chance.of(NUMBERS_CHANCE);
        }
        if (numbersChance.isAlways()) {
            setBeginWithLetter(false);
        }
        this.numberChance = numbersChance;
        return this;
    }

    @Override
    public PasswordSettings setSpecialCharacters(String specialCharacters) {
        if (specialCharacters == null || specialCharacters.isEmpty()) {
            specialCharacters = SPECIAL_CHARACTERS;
        }
        this.specialCharacters = specialCharacters;
        return this;
    }

    @Override
    public PasswordSettings setSpecialCharacterChance(Chance specialCharacterChance) {
        if (specialCharacterChance == null) {
            specialCharacterChance = Chance.of(SPECIAL_CHARACTER_CHANCE_PERCENTAGE);
        }
        if (specialCharacterChance.isAlways()) {
            setBeginWithLetter(false);
        }
        this.specialCharacterChance = specialCharacterChance;
        return this;
    }

    @Override
    public PasswordSettings setOnlyLowercase(boolean onlyLowercase) {
        this.onlyLowercase = onlyLowercase;
        return this;
    }

    @Override
    public PasswordSettings setExcludeSimilarCharacters(boolean excludeSimilarCharacters) {
        if (excludeSimilarCharacters) {
            String pool = removeSimilarCharacters(alphabet());
            if (pool.isEmpty()) {
                pool = removeSimilarCharacters(DefaultSettings.ENGLISH_ALPHABET);
            }
            setAlphabet(pool);

            pool = removeSimilarCharacters(numbers());
            if (pool.isEmpty()) {
                pool = removeSimilarCharacters(DefaultSettings.DECIMAL_NUMBERS);
            }
            setNumbers(pool);

            pool = removeSimilarCharacters(specialCharacters());
            if (pool.isEmpty()) {
                pool = removeSimilarCharacters(DefaultSettings.SPECIAL_CHARACTERS);
            }
            setSpecialCharacters(pool);
        }
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
        this.beginWithLetter = beginWithLetter && (numberChance.chancePercentage() + specialCharacterChance().chancePercentage() < 100);
        return this;
    }

    @Override
    public PasswordSettings setNoDuplicates(boolean noDuplicates) {
        if (!noDuplicates) {
            this.noDuplicates = false;
        } else {
            // check that it's possible to create a password with no dupes
            boolean enoughLetters =
                    Chance.opposite(numbersChance())
                            .getPercentageAmount(passwordLength())
                            <= alphabet().length();
            boolean enoughNumbers = numbersChance()
                    .getPercentageAmount(passwordLength())
                    <= numbers().length();
            boolean enoughSpecials = specialCharacterChance()
                    .getPercentageAmount(passwordLength())
                    <= specialCharacters().length();
            this.noDuplicates = enoughLetters && enoughNumbers && enoughSpecials;
        }
        return this;
    }
}
