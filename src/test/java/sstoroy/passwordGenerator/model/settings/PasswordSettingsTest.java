package sstoroy.passwordGenerator.model.settings;

import org.junit.jupiter.api.Test;
import sstoroy.passwordGenerator.model.chance.Chance;

import static org.junit.jupiter.api.Assertions.*;

class PasswordSettingsTest {

    @Test
    void correctCharacterPools() {
        PasswordSettings settings = new PasswordSettingsImpl();

        String expectedAlphabet = "abcdef";
        String expectedNumbers = "123456789";
        String expectedSpecials = "|!\"#¤%&/()=?";
        settings.setAlphabet(expectedAlphabet);
        settings.setNumbers(expectedNumbers);
        settings.setSpecialCharacters(expectedSpecials);
        assertEquals(expectedAlphabet, settings.alphabet());
        assertEquals(expectedNumbers, settings.numbers());
        assertEquals(expectedSpecials, settings.specialCharacters());

        expectedAlphabet = "a";
        expectedNumbers = "0";
        expectedSpecials = "!";
        settings.setAlphabet(expectedAlphabet);
        settings.setNumbers(expectedNumbers);
        settings.setSpecialCharacters(expectedSpecials);
        assertEquals(expectedAlphabet, settings.alphabet());
        assertEquals(expectedNumbers, settings.numbers());
        assertEquals(expectedSpecials, settings.specialCharacters());

        settings.setAlphabet("aaa");
        assertEquals(expectedAlphabet, settings.alphabet());

        settings.setAlphabet("000a000");
        settings.setNumbers("000a000");
        assertEquals(expectedAlphabet, settings.alphabet());
        assertEquals(expectedNumbers, settings.numbers());

        settings.setAlphabet(" ");
        settings.setNumbers(" ");
        settings.setSpecialCharacters(" ");
        assertEquals(DefaultSettings.ENGLISH_ALPHABET, settings.alphabet());
        assertEquals(DefaultSettings.DECIMAL_NUMBERS, settings.numbers());
        assertEquals(" ", settings.specialCharacters());

        settings.setAlphabet(null);
        settings.setNumbers(null);
        settings.setSpecialCharacters(null);
        assertEquals(DefaultSettings.ENGLISH_ALPHABET, settings.alphabet());
        assertEquals(DefaultSettings.DECIMAL_NUMBERS, settings.numbers());
        assertEquals(DefaultSettings.SPECIAL_CHARACTERS, settings.specialCharacters());

        settings.setAlphabet("");
        settings.setNumbers("");
        settings.setSpecialCharacters("");
        assertEquals(DefaultSettings.ENGLISH_ALPHABET, settings.alphabet());
        assertEquals(DefaultSettings.DECIMAL_NUMBERS, settings.numbers());
        assertEquals(DefaultSettings.SPECIAL_CHARACTERS, settings.specialCharacters());

        settings.setAlphabet("123!?+");
        settings.setNumbers("abc!?+");
        assertEquals(DefaultSettings.ENGLISH_ALPHABET, settings.alphabet());
        assertEquals(DefaultSettings.DECIMAL_NUMBERS, settings.numbers());
    }

    @Test
    void excludeSimilarCharacters() {
        PasswordSettings settings = new PasswordSettingsImpl();
        settings.setExcludeSimilarCharacters(true);
        String expectedAlphabet = "abcdefghjkmnprstuvwxyz";
        String expectedNumbers = "23456789";
        String expectedSpecials = DefaultSettings.SPECIAL_CHARACTERS;
        assertEquals(expectedAlphabet, settings.alphabet());
        assertEquals(expectedNumbers, settings.numbers());
        assertEquals(expectedSpecials, settings.specialCharacters());

        settings.setAlphabet("io");
        settings.setNumbers("01");
        settings.setExcludeSimilarCharacters(true);
        assertEquals(expectedAlphabet, settings.alphabet());
        assertEquals(expectedNumbers, settings.numbers());
        assertEquals(expectedSpecials, settings.specialCharacters());

        settings.setAlphabet("abcidef");
        settings.setNumbers("2031");
        settings.setSpecialCharacters("!l?");
        settings.setExcludeSimilarCharacters(true);
        assertEquals("abcdef", settings.alphabet());
        assertEquals("23", settings.numbers());
        assertEquals("!?", settings.specialCharacters());
    }

    @Test
    void setFirstLetter() {
        PasswordSettings settings = new PasswordSettingsImpl();
        assertTrue(settings.beginWithLetter());
        settings.setNumbersChance(Chance.of(100));
        assertFalse(settings.beginWithLetter());

        settings.setNumbersChance(Chance.of(0));
        assertFalse(settings.beginWithLetter());
        settings.setBeginWithLetter(true);
        assertTrue(settings.beginWithLetter());
        settings.setNumbersChance(Chance.of(100));
        settings.setBeginWithLetter(true);
        assertFalse(settings.beginWithLetter());
    }

    @Test
    void avoidDupes() {
        PasswordSettings settings = new PasswordSettingsImpl()
            .setAlphabet("abcde")
            .setNumbers("12345")
            .setNumbersChance(Chance.of(50))
            .setSpecialCharacters("!")
            .setPasswordLength(12)
            .setNoDuplicates(true);
        assertFalse(settings.noDuplicates());

        settings.setPasswordLength(8)
                .setSpecialCharacterChance(Chance.never())
                .setNoDuplicates(true);
        assertTrue(settings.noDuplicates());

        settings.setPasswordLength(20);
        assertFalse(settings.noDuplicates());

        settings = new PasswordSettingsImpl()
            .setNumbersChance(Chance.never())
            .setSpecialCharacterChance(Chance.never())
            .setPasswordLength(30)
            .setNoDuplicates(true);
        assertFalse(settings.noDuplicates());
    }
}