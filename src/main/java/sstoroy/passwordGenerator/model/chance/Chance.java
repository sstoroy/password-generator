package sstoroy.passwordGenerator.model.chance;

/**
 * A class that holds a percentage of something, aka. a value between 0 and 100 inclusive.
 */
public interface Chance {
    /** Creates a new Chance object by trying to parse a String */
    static Chance of(String chance) {
        return of(Integer.parseInt(chance));
    }

    /** Creates a new Chance object with a given value between 0 and 100 inclusive.
     *  Values outside the scope will be ignored and the nearest edge value is chosen:
     *  Negative values = 0, Values > 100 = 100 */
    static Chance of(int chance) {
        if (chance <= 0) return never();
        if (chance >= 100) return always();
        return new ChancePercentage(chance);
    }

    /** Creates a new Chance object by subtracting the given chance values
     * to ensure the total chance is 100%.  */
    static Chance opposite(Chance... chance) {
        int sum = 100;
        for (Chance c : chance) {
            sum -= c.chancePercentage();
        }
        return new ChancePercentage(sum);
    }

    /** Creates a Chance object that will never happen */
    static Chance never() {
        return ChancePercentage.NEVER;
    }

    /** Creates a new Chance that will always happen */
    static Chance always() {
        return ChancePercentage.ALWAYS;
    }

    /** @return the chance value as a percentage 0-100 */
    int chancePercentage();
    /** @return true if the chance is > 0, false otherwise */
    boolean isPossible();
    /** @return true if the chance is always true, false otherwise */
    boolean isAlways();
    /** Takes the given amount and returns the amount in percentage.
     * For instance, if the chance is 10% and the amount is 100, will
     * return 10. */
    int getPercentageAmount(int amount);
}

