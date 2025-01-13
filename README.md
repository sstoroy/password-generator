# Password Generator

Built with [Spring Boot](https://spring.io/projects/spring-boot)

A web service that creates random passwords from certain parameters:
- `amount` How many passwords to generate (default 1, min 1, max 50).
- `length` Length of password (default 8, min 6, max 128).
- `letters` Available letters for the password (default English alphabet).
- `numbers` Available numbers for the password (default 0-9).
- `number_chance` How much of the password are numbers. If 100%, the application becomes a random number generator, and the minimum length is 1 (default 25%, min 0%, max 100%).
- `symbols` Available symbols for the password (default are the common ones).
- `symbol_chance` How much of the password are symbols (default 25%, min 0%, max 100%).
- `only_lowercase` Disable uppercase letters in the password (default false).
- `begin_with_letter` Ensure that the first character in the password is a letter. If the number and symbols chance are set so the password can't contain letters, this setting will not take effect (default true).
- `no_duplicates` Ensure that no characters are repeated in the password. If it's impossible to make a password from the letters, numbers and symbols available (for instance with large password lengths), this setting will not take effect (default false).
- `exclude_similar` Removes similar characters from available letters, numbers and symbols (default false).

These parameters can be used in the URL of the application site, for instance `<websiteURL>/?amount=10&length=20&letters=abcd&symbols=!%20%3C` (notice how special characters might need to be encoded to work in the URL).
The site will then open with these settings set, and the missing parameters (if any) set to default.

## API

The application is RESTful, with a few links that handles POST:
- `/api/index` returns a JSON with all the settings used, the password(s) created and a parameter URL that can be copied and pasted to the main page.
- `/api/passwords` returns a JSON with the password(s) generated
- `/api/settings` returns a JSON with the settings used for password generation
- `/api/params` returns a string with just the parameters for use in  the URL

The JSON for POST must include a key `"settings"` where the value is an object of each setting and their corresponding value. 
Other fields and incompatible values are ignored.