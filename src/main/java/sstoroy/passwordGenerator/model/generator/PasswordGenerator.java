package sstoroy.passwordGenerator.model.generator;

/**
 * A class for generating passwords. Implementations of this class decides how to
 * create the passwords on their own. Only has one method, generate().
 */
public interface PasswordGenerator {
    /** @return the generated password */
    String generate();
}
