package sstoroy.passwordGenerator.model.generator;

import org.junit.jupiter.api.Test;
import sstoroy.passwordGenerator.model.chance.Chance;
import sstoroy.passwordGenerator.model.settings.DefaultSettings;
import sstoroy.passwordGenerator.model.settings.PasswordSettings;
import sstoroy.passwordGenerator.model.settings.PasswordSettingsImpl;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {
    private final Set<Character> NUMBERS;
    private final Set<Character> LOWERCASE_LETTERS;
    private final Set<Character> UPPERCASE_LETTERS;
    private final Set<Character> SPECIAL_CHARACTERS;
    private final int PWD_AMOUNT_TESTS = 1000;
    private final Random random = new Random();

    PasswordGeneratorTest() {
        this.NUMBERS = new HashSet<>();
        for (Character c: DefaultSettings.DECIMAL_NUMBERS.toCharArray()) {
            this.NUMBERS.add(c);
        }
        this.LOWERCASE_LETTERS = new HashSet<>();
        for (Character c: DefaultSettings.ENGLISH_ALPHABET.toCharArray()) {
            this.LOWERCASE_LETTERS.add(c);
        }
        this.UPPERCASE_LETTERS = new HashSet<>();
        for (Character c: "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
            this.UPPERCASE_LETTERS.add(c);
        }
        this.SPECIAL_CHARACTERS = new HashSet<>();
        for (Character c: DefaultSettings.SPECIAL_CHARACTERS.toCharArray()) {
            this.SPECIAL_CHARACTERS.add(c);
        }
    }

    @Test
    void populateWithLowercaseLetters() {
        PasswordSettings pwdSettings = new PasswordSettingsImpl()
                .setNumbersChance(Chance.of(0))
                .setSpecialCharacterChance(Chance.of(0))
                .setOnlyLowercase(true);
        PasswordGenerator pwdg = new PasswordGeneratorImpl(pwdSettings);
        for (int i=0;i<PWD_AMOUNT_TESTS;i++) {
            int pwdLength = random.nextInt(DefaultSettings.MIN_LENGTH, DefaultSettings.MAX_LENGTH);
            pwdSettings.setPasswordLength(pwdLength);
            String pwd = pwdg.generate();
            assertEquals(pwdLength, pwd.length());
            for (Character c : pwd.toCharArray()) {
                assertTrue(LOWERCASE_LETTERS.contains(c));
                assertFalse(UPPERCASE_LETTERS.contains(c));
            }
        }
    }

    @Test
    void populateWithLetters() {
        PasswordSettings pwdSettings = new PasswordSettingsImpl()
                .setNumbersChance(Chance.of(0))
                .setSpecialCharacterChance(Chance.of(0));
        PasswordGenerator pwdg = new PasswordGeneratorImpl(pwdSettings);
        for (int i=0;i<PWD_AMOUNT_TESTS;i++) {
            int pwdLength = random.nextInt(DefaultSettings.MIN_LENGTH*2, DefaultSettings.MAX_LENGTH);
            // make the min length double to avoid randomly not creating uppercase letters
            int sumLowercase = 0;
            int sumUppercase = 0;
            pwdSettings.setPasswordLength(pwdLength);
            String pwd = pwdg.generate();
            assertEquals(pwdLength, pwd.length());
            for (Character c : pwd.toCharArray()) {
                if (LOWERCASE_LETTERS.contains(c)) {
                    sumLowercase++;
                } else if (UPPERCASE_LETTERS.contains(c)) {
                    sumUppercase++;
                }
            }
            assertTrue(sumLowercase > 0);
            assertTrue(sumUppercase > 0);
            assertEquals(pwdLength, sumLowercase + sumUppercase);
        }
    }

    @Test
    void populateWithNumbers() {
        PasswordSettings pwdSettings = new PasswordSettingsImpl()
                .setNumbersChance(Chance.of(100))
                .setSpecialCharacterChance(Chance.of(0));
        PasswordGenerator pwdg = new PasswordGeneratorImpl(pwdSettings);
        for (int i=0;i<PWD_AMOUNT_TESTS;i++) {
            int pwdLength = random.nextInt(DefaultSettings.MIN_LENGTH_NO, DefaultSettings.MAX_LENGTH);
            pwdSettings.setPasswordLength(pwdLength);
            String pwd = pwdg.generate();
            assertEquals(pwdLength, pwd.length());
            for (Character c : pwd.toCharArray()) {
                assertTrue(NUMBERS.contains(c));
            }
        }
    }

    @Test
    void populateWithSpecials() {
        PasswordSettings pwdSettings = new PasswordSettingsImpl()
                .setNumbersChance(Chance.of(0))
                .setSpecialCharacterChance(Chance.of(100))
                .setBeginWithLetter(false);
        PasswordGenerator pwdg = new PasswordGeneratorImpl(pwdSettings);
        for (int i=0;i<PWD_AMOUNT_TESTS;i++) {
            int pwdLength = random.nextInt(DefaultSettings.MIN_LENGTH, DefaultSettings.MAX_LENGTH);
            pwdSettings.setPasswordLength(pwdLength);
            String pwd = pwdg.generate();
            assertEquals(pwdLength, pwd.length());
            for (Character c : pwd.toCharArray()) {
                assertTrue(SPECIAL_CHARACTERS.contains(c));
            }
        }
    }

    @Test
    void mixLettersNumbers() {
        PasswordSettings pwdSettings = new PasswordSettingsImpl()
                .setNumbersChance(Chance.of(50))
                .setSpecialCharacterChance(Chance.of(0))
                .setBeginWithLetter(false);
        PasswordGenerator pwdg = new PasswordGeneratorImpl(pwdSettings);
        for (int i=0;i<PWD_AMOUNT_TESTS;i++) {
            int pwdLength = random.nextInt(DefaultSettings.MIN_LENGTH, DefaultSettings.MAX_LENGTH);
            int sumLetters = 0;
            int sumNumbers = 0;
            pwdSettings.setPasswordLength(pwdLength);
            String pwd = pwdg.generate();
            assertEquals(pwdLength, pwd.length());
            for (Character c : pwd.toCharArray()) {
                if (LOWERCASE_LETTERS.contains(c) || UPPERCASE_LETTERS.contains(c)) {
                    sumLetters++;
                } else if (NUMBERS.contains(c)) {
                    sumNumbers++;
                }
            }
            assertTrue(sumLetters > 0);
            assertTrue(sumNumbers > 0);
            assertEquals(pwdLength, sumLetters + sumNumbers);
        }
    }

    @Test
    void mixLettersNumbersSpecials() {
        PasswordSettings pwdSettings = new PasswordSettingsImpl()
                .setNumbersChance(Chance.of(25))
                .setSpecialCharacterChance(Chance.of(25))
                .setBeginWithLetter(false);
        PasswordGenerator pwdg = new PasswordGeneratorImpl(pwdSettings);
        for (int i=0;i<PWD_AMOUNT_TESTS;i++) {
            int pwdLength = random.nextInt(DefaultSettings.MIN_LENGTH, DefaultSettings.MAX_LENGTH);
            int sumLetters = 0;
            int sumNumbers = 0;
            int sumSpecials = 0;
            pwdSettings.setPasswordLength(pwdLength);
            String pwd = pwdg.generate();
            assertEquals(pwdLength, pwd.length());
            for (Character c : pwd.toCharArray()) {
                if (LOWERCASE_LETTERS.contains(c) || UPPERCASE_LETTERS.contains(c)) {
                    sumLetters++;
                } else if (NUMBERS.contains(c)) {
                    sumNumbers++;
                } else if (SPECIAL_CHARACTERS.contains(c)) {
                    sumSpecials++;
                }
            }
            assertTrue(sumLetters > 0);
            assertTrue(sumNumbers > 0);
            assertTrue(sumSpecials > 0);
            assertEquals(pwdLength, sumLetters + sumNumbers + sumSpecials);
        }
    }

    @Test
    void firstIsLetter() {
        PasswordSettings pwdSettings = new PasswordSettingsImpl()
                .setNumbersChance(Chance.of(50));
        PasswordGenerator pwdg = new PasswordGeneratorImpl(pwdSettings);
        for (int i=0;i<PWD_AMOUNT_TESTS;i++) {
            int pwdLength = random.nextInt(DefaultSettings.MIN_LENGTH, DefaultSettings.MAX_LENGTH);
            Chance specialChance = Chance.of(random.nextInt(49,51));
            pwdSettings.setPasswordLength(pwdLength)
                .setSpecialCharacterChance(specialChance)
                .setBeginWithLetter(true);
            String pwd = pwdg.generate();
            assertEquals(pwdLength, pwd.length());
            if (specialChance.chancePercentage() >= 50) { // equals no chance of letter
                assertFalse(pwdSettings.beginWithLetter());
                assertFalse(LOWERCASE_LETTERS.contains(pwd.charAt(0)));
            } else {
                assertTrue(pwdSettings.beginWithLetter());
                char at = pwd.charAt(0);
                assertTrue(LOWERCASE_LETTERS.contains(at) || UPPERCASE_LETTERS.contains(at));
            }
        }
    }

    @Test
    void noSimilars() {
        PasswordSettings pwdSettings = new PasswordSettingsImpl()
                .setNumbersChance(Chance.of(0))
                .setSpecialCharacterChance(Chance.of(0))
                .setExcludeSimilarCharacters(true);
        assertEquals("abcdefghjkmnprstuvwxyz", pwdSettings.alphabet());
        assertNotEquals(DefaultSettings.DECIMAL_NUMBERS, pwdSettings.numbers());

        Set<Character> similars = DefaultSettings.SIMILAR_CHARACTERS;
        PasswordGenerator pwdg = new PasswordGeneratorImpl(pwdSettings);
        for (int i=0;i<PWD_AMOUNT_TESTS;i++) {
            int pwdLength = random.nextInt(DefaultSettings.MIN_LENGTH, DefaultSettings.MAX_LENGTH);
            pwdSettings.setPasswordLength(pwdLength);
            String pwd = pwdg.generate();
            assertEquals(pwdLength, pwd.length());
            for (Character c : pwd.toCharArray()) {
                assertFalse(similars.contains(c));
            }
        }
    }

    @Test
    void noDuplicates() {
        PasswordSettings pwdSettings = new PasswordSettingsImpl();
        PasswordGenerator pwdg = new PasswordGeneratorImpl(pwdSettings);
        for (int i=0;i<PWD_AMOUNT_TESTS;i++) {
            int pwdLength = random.nextInt(DefaultSettings.MIN_LENGTH, DefaultSettings.MAX_LENGTH/2);
            pwdSettings.setPasswordLength(pwdLength)
                    .setNoDuplicates(true);

            // if for some reason dupes are not possible
            // (most likely password too long), disregard
            if (!pwdSettings.noDuplicates()) {
                continue;
            }
            Set<Character> duplicates = new HashSet<>();
            String pwd = pwdg.generate();
            assertEquals(pwdLength, pwd.length());
            for (Character c : pwd.toCharArray()) {
                assertFalse(duplicates.contains(c));
                duplicates.add(c);
            }
        }
    }
}