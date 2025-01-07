package sstoroy.passwordGenerator.model.password;

import sstoroy.passwordGenerator.model.chance.Chance;

import java.util.*;

public class PasswordBuilderImpl implements PasswordBuilder {
    private final char[] password;
    private final Collection<Integer> numbersIndexes;
    private final Collection<Integer> lettersIndexes;
    private final Collection<Integer> specialsIndexes;

    public PasswordBuilderImpl(int length, Chance numbersChance, Chance specialCharactersChance) {
        this.password = new char[length];
        Arrays.fill(password, ' ');

        Chance lettersChance = Chance.of(
                100
                - numbersChance.chancePercentage()
                - specialCharactersChance.chancePercentage()
        );

        this.numbersIndexes = new HashSet<>();
        this.specialsIndexes = new HashSet<>();
        this.lettersIndexes = new HashSet<>();
        assignIndexes(length, numbersChance, specialCharactersChance, lettersChance);
    }

    private static List<Integer> getListIntegersFromRange(int length) {
        List<Integer> ints = new ArrayList<>(length);
        for (int i=0; i<length; i++) {
            ints.add(i);
        }
        return ints;
    }

    private void assignIndexes(int length, Chance numbersChance, Chance specialCharactersChance, Chance lettersChance) {
        // find out how many of each category we need
        int numNumbers = numbersChance.getPercentageAmount(length);
        int numSpecials = specialCharactersChance.getPercentageAmount(length);
        int numLetters = lettersChance.getPercentageAmount(length);
        while (numNumbers + numSpecials + numLetters < length) {
            if (lettersChance.isPossible()) {
                numLetters++;
            } else {
                numNumbers++;
            }
        }

        // create pool of all available indexes
        // the pool will empty out as indexes are taken
        List<Integer> pool = getListIntegersFromRange(length);
        Random rand = new Random();

        // first for numbers
        for (int i=0; i<numNumbers; i++) {
            int randomPoolIndex = rand.nextInt(pool.size());
            int randomIndex = pool.remove(randomPoolIndex);
            this.numbersIndexes.add(randomIndex);
        }

        // then letters
        for (int i=0; i<numLetters; i++) {
            int randomPoolIndex = rand.nextInt(pool.size());
            int randomIndex = pool.remove(randomPoolIndex);
            this.lettersIndexes.add(randomIndex);
        }

        // lastly, special characters
        for (int i=0; i<numSpecials; i++) {
            int randomPoolIndex = rand.nextInt(pool.size());
            int randomIndex = pool.remove(randomPoolIndex);
            this.specialsIndexes.add(randomIndex);
        }
    }

    @Override
    public void set(int index, char character) {
        this.password[index] = character;
    }


    @Override
    public Iterable<Integer> numberIndexes() {
        return Collections.unmodifiableCollection(numbersIndexes);
    }

    @Override
    public Iterable<Integer> letterIndexes() {
        return Collections.unmodifiableCollection(lettersIndexes);
    }

    @Override
    public Iterable<Integer> specialIndexes() {
        return Collections.unmodifiableCollection(specialsIndexes);
    }

    @Override
    public String toString() {
        return new String(password);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof PasswordBuilderImpl that)) return false;

        return Arrays.equals(password, that.password) && numbersIndexes.equals(that.numbersIndexes) && lettersIndexes.equals(that.lettersIndexes) && specialsIndexes.equals(that.specialsIndexes);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(password);
        result = 31 * result + numbersIndexes.hashCode();
        result = 31 * result + lettersIndexes.hashCode();
        result = 31 * result + specialsIndexes.hashCode();
        return result;
    }
}
