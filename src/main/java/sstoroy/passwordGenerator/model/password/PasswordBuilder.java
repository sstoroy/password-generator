package sstoroy.passwordGenerator.model.password;

/**
 * A representation of the building blocks of the password. To see
 * the current password, use toString().
 */
public interface PasswordBuilder {
    /** set a character at the given index */
    void set(int index, char character);
    /** @return an iterable of integers representing the spaces reserved for numbers */
    Iterable<Integer> numberIndexes();
    /** @return an iterable of integers representing the spaces reserved for letters */
    Iterable<Integer> letterIndexes();
    /** @return an iterable of integers representing the spaces reserved for special characters */
    Iterable<Integer> specialIndexes();
}
