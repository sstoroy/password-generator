package sstoroy.passwordGenerator.model.settings;

import org.junit.jupiter.api.Test;
import sstoroy.passwordGenerator.model.chance.Chance;
import sstoroy.passwordGenerator.model.settings.manager.DefaultSettings;
import sstoroy.passwordGenerator.model.settings.manager.PasswordSettings;
import sstoroy.passwordGenerator.model.settings.manager.PasswordSettingsImpl;

import static org.junit.jupiter.api.Assertions.*;

class PasswordSettingsTest {

    @Test
    void defaultSettings() {
        for (SettingKey key : SettingKey.values()) {
            assertNotNull(DefaultSettings.getDefaultSetting(key));
        }
    }

    @Test
    void correctCharacterPools() {
        PasswordSettings settings = new PasswordSettingsImpl();

        String expectedAlphabet = "abcdef";
        String expectedNumbers = "123456789";
        String expectedSpecials = "|!\"#¤%&/()=?";
        settings.setAlphabet(expectedAlphabet);
        settings.setNumbers(expectedNumbers);
        settings.setSymbols(expectedSpecials);
        assertEquals(expectedAlphabet, settings.alphabet());
        assertEquals(expectedNumbers, settings.numbers());
        assertEquals(expectedSpecials, settings.symbols());

        expectedAlphabet = "a";
        expectedNumbers = "0";
        expectedSpecials = "!";
        settings.setAlphabet(expectedAlphabet);
        settings.setNumbers(expectedNumbers);
        settings.setSymbols(expectedSpecials);
        assertEquals(expectedAlphabet, settings.alphabet());
        assertEquals(expectedNumbers, settings.numbers());
        assertEquals(expectedSpecials, settings.symbols());

        settings.setAlphabet("aaa");
        assertEquals(expectedAlphabet, settings.alphabet());

        settings.setAlphabet("000a000");
        settings.setNumbers("000a000");
        assertEquals(expectedAlphabet, settings.alphabet());
        assertEquals(expectedNumbers, settings.numbers());

        settings.setAlphabet(" ");
        settings.setNumbers(" ");
        settings.setSymbols(" ");
        assertEquals(DefaultSettings.getDefaultSetting(SettingKey.ALPHABET).getStringUnsafe(), settings.alphabet());
        assertEquals(DefaultSettings.getDefaultSetting(SettingKey.NUMBERS).getStringUnsafe(), settings.numbers());
        assertEquals(" ", settings.symbols());

        settings.setAlphabet(null);
        settings.setNumbers(null);
        settings.setSymbols(null);
        assertEquals(DefaultSettings.getDefaultSetting(SettingKey.ALPHABET).getStringUnsafe(), settings.alphabet());
        assertEquals(DefaultSettings.getDefaultSetting(SettingKey.NUMBERS).getStringUnsafe(), settings.numbers());
        assertEquals(DefaultSettings.getDefaultSetting(SettingKey.SYMBOLS).getStringUnsafe(), settings.symbols());

        settings.setAlphabet("");
        settings.setNumbers("");
        settings.setSymbols("");
        assertEquals(DefaultSettings.getDefaultSetting(SettingKey.ALPHABET).getStringUnsafe(), settings.alphabet());
        assertEquals(DefaultSettings.getDefaultSetting(SettingKey.NUMBERS).getStringUnsafe(), settings.numbers());
        assertEquals(DefaultSettings.getDefaultSetting(SettingKey.SYMBOLS).getStringUnsafe(), settings.symbols());

        settings.setAlphabet("123!?+");
        settings.setNumbers("abc!?+");
        assertEquals(DefaultSettings.getDefaultSetting(SettingKey.ALPHABET).getStringUnsafe(), settings.alphabet());
        assertEquals(DefaultSettings.getDefaultSetting(SettingKey.NUMBERS).getStringUnsafe(), settings.numbers());
    }

    @Test
    void excludeSimilarCharacters() {
        PasswordSettings settings = new PasswordSettingsImpl();
        settings.setExcludeSimilarCharacters(true);
        String expectedAlphabet = "abcdefghjkmnprstuvwxyz";
        String expectedNumbers = "23456789";
        String expectedSpecials = DefaultSettings.getDefaultSetting(SettingKey.SYMBOLS).getStringUnsafe();
        assertEquals(expectedAlphabet, settings.alphabet());
        assertEquals(expectedNumbers, settings.numbers());
        assertEquals(expectedSpecials, settings.symbols());

        settings.setAlphabet("io");
        settings.setNumbers("01");
        settings.setExcludeSimilarCharacters(true);
        assertEquals(expectedAlphabet, settings.alphabet());
        assertEquals(expectedNumbers, settings.numbers());
        assertEquals(expectedSpecials, settings.symbols());

        settings.setAlphabet("abcidef");
        settings.setNumbers("2031");
        settings.setSymbols("!l?");
        settings.setExcludeSimilarCharacters(true);
        assertEquals("abcdef", settings.alphabet());
        assertEquals("23", settings.numbers());
        assertEquals("!?", settings.symbols());
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
            .setSymbols("!")
            .setPasswordLength(12)
            .setNoDuplicates(true);
        assertFalse(settings.noDuplicates());

        settings.setPasswordLength(8)
                .setSymbolsChance(Chance.never())
                .setNoDuplicates(true);
        assertTrue(settings.noDuplicates());

        settings.setPasswordLength(20);
        assertFalse(settings.noDuplicates());

        settings = new PasswordSettingsImpl()
            .setNumbersChance(Chance.never())
            .setSymbolsChance(Chance.never())
            .setPasswordLength(30)
            .setNoDuplicates(true);
        assertFalse(settings.noDuplicates());
    }
}