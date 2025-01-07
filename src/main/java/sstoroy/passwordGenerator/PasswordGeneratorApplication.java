package sstoroy.passwordGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sstoroy.passwordGenerator.model.generator.PasswordGenerator;
import sstoroy.passwordGenerator.model.generator.PasswordGeneratorImpl;
import sstoroy.passwordGenerator.model.chance.Chance;
import sstoroy.passwordGenerator.model.settings.DefaultSettings;
import sstoroy.passwordGenerator.model.settings.PasswordSettings;
import sstoroy.passwordGenerator.model.settings.PasswordSettingsImpl;

@SpringBootApplication
@RestController
public class PasswordGeneratorApplication {
	private final static int MAX_PASSWORD_AMOUNT = 100;

	public static void main(String[] args) {
		SpringApplication.run(PasswordGeneratorApplication.class, args);
	}

	@GetMapping("/")
	public String index() {
		return "Hei";
	}

	@GetMapping("/generate")
	public String generate(
			@RequestParam(value = "amount", defaultValue = "1") int amount,
			@RequestParam(value = "length", defaultValue = DefaultSettings.PASSWORD_LENGTH) int length,
			@RequestParam(value = "alphabet", defaultValue = DefaultSettings.ENGLISH_ALPHABET) String alphabet,
			@RequestParam(value = "numbers", defaultValue = DefaultSettings.DECIMAL_NUMBERS) String numbers,
			@RequestParam(value = "numberChance", defaultValue = DefaultSettings.NUMBERS_CHANCE) int numberChance,
			@RequestParam(value = "special", defaultValue = DefaultSettings.SPECIAL_CHARACTERS) String special,
			@RequestParam(value = "specialCharactersChance", defaultValue = DefaultSettings.SPECIAL_CHARACTER_CHANCE_PERCENTAGE) int specialCharactersChance,
			@RequestParam(value = "only_lowercase", defaultValue = DefaultSettings.ONLY_LOWERCASE) boolean onlyLowercase,
			@RequestParam(value = "begin_with_letter", defaultValue = DefaultSettings.BEGIN_WITH_LETTER) boolean beginWithLetter,
			@RequestParam(value = "noDuplicates", defaultValue = DefaultSettings.NUMBERS_CHANCE) boolean noDuplicates,
			@RequestParam(value = "exclude_similar", defaultValue = DefaultSettings.EXCLUDE_SIMILAR_CHARACTERS) boolean excludeSimilarCharacters
			) {
		if (amount > MAX_PASSWORD_AMOUNT) amount = MAX_PASSWORD_AMOUNT;

		PasswordSettings settings = new PasswordSettingsImpl()
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
		PasswordGenerator generator = new PasswordGeneratorImpl(settings);
		StringBuilder passwords = new StringBuilder();
		for (int i=0;i<amount;i++) {
			passwords.append(generator.generate()).append("\n");
		}
		return passwords.toString();
	}
}
