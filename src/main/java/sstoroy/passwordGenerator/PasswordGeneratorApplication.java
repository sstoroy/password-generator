package sstoroy.passwordGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sstoroy.passwordGenerator.controller.PasswordGeneratorResponse;
import sstoroy.passwordGenerator.controller.PasswordGeneratorResponseImpl;
import sstoroy.passwordGenerator.model.chance.Chance;
import sstoroy.passwordGenerator.model.settings.DefaultSettings;
import sstoroy.passwordGenerator.model.settings.PasswordSettings;
import sstoroy.passwordGenerator.model.settings.PasswordSettingsImpl;

import java.util.Map;

@SpringBootApplication
@RestController
public class PasswordGeneratorApplication {
	private final static int MIN_PASSWORD_AMOUNT = 1;
	private final static int MAX_PASSWORD_AMOUNT = 100;

	public static void main(String[] args) {
		SpringApplication.run(PasswordGeneratorApplication.class, args);
	}

	@GetMapping("/generate")
	public PasswordGeneratorResponse generate(
			@RequestParam(value = PasswordGeneratorResponse.SETTINGS_AMOUNT_PASSWORDS, defaultValue = "1") int amount,
			@RequestParam(value = PasswordGeneratorResponse.SETTINGS_PASSWORD_LENGTH, defaultValue = DefaultSettings.PASSWORD_LENGTH) int length,
			@RequestParam(value = PasswordGeneratorResponse.SETTINGS_ALPHABET, defaultValue = DefaultSettings.ENGLISH_ALPHABET) String alphabet,
			@RequestParam(value = PasswordGeneratorResponse.SETTINGS_NUMBERS, defaultValue = DefaultSettings.DECIMAL_NUMBERS) String numbers,
			@RequestParam(value = PasswordGeneratorResponse.SETTINGS_NUMBERS_CHANCE, defaultValue = DefaultSettings.NUMBERS_CHANCE_PERCENTAGE) int numberChance,
			@RequestParam(value = PasswordGeneratorResponse.SETTINGS_SPECIAL_CHARACTERS, defaultValue = DefaultSettings.SPECIAL_CHARACTERS) String special,
			@RequestParam(value = PasswordGeneratorResponse.SETTINGS_SPECIAL_CHANCE, defaultValue = DefaultSettings.SPECIAL_CHARACTER_CHANCE_PERCENTAGE) int specialCharactersChance,
			@RequestParam(value = PasswordGeneratorResponse.SETTINGS_ONLY_LOWERCASE, defaultValue = DefaultSettings.ONLY_LOWERCASE) boolean onlyLowercase,
			@RequestParam(value = PasswordGeneratorResponse.SETTINGS_BEGIN_WITH_LETTER, defaultValue = DefaultSettings.BEGIN_WITH_LETTER) boolean beginWithLetter,
			@RequestParam(value = PasswordGeneratorResponse.SETTINGS_NO_DUPLICATES, defaultValue = DefaultSettings.NO_DUPLICATES) boolean noDuplicates,
			@RequestParam(value = PasswordGeneratorResponse.SETTINGS_EXCLUDE_SIMILAR, defaultValue = DefaultSettings.EXCLUDE_SIMILAR_CHARACTERS) boolean excludeSimilarCharacters
	) {
		if (amount <= MIN_PASSWORD_AMOUNT) amount = MIN_PASSWORD_AMOUNT;
		else if (amount > MAX_PASSWORD_AMOUNT) amount = MAX_PASSWORD_AMOUNT;

		PasswordSettings settings = createSettingsFromParameters(
				length, alphabet, numbers, numberChance,
				special, specialCharactersChance, onlyLowercase,
				beginWithLetter, noDuplicates, excludeSimilarCharacters);
		return new PasswordGeneratorResponseImpl(amount, settings);
	}

	private PasswordSettings createSettingsFromParameters(
			int length, String alphabet, String numbers, int numberChance,
			String special, int specialCharactersChance, boolean onlyLowercase,
			boolean beginWithLetter, boolean noDuplicates, boolean excludeSimilarCharacters
	) {
		return new PasswordSettingsImpl()
				.setAlphabet(alphabet)
				.setExcludeSimilarCharacters(excludeSimilarCharacters)
				.setNumbers(numbers)
				.setNumbersChance(Chance.of(numberChance))
				.setSpecialCharacters(special)
				.setSpecialCharacterChance(Chance.of(specialCharactersChance))
				.setOnlyLowercase(onlyLowercase)
				.setBeginWithLetter(beginWithLetter)
				.setNoDuplicates(noDuplicates)
				.setPasswordLength(length)
				;
	}
}
