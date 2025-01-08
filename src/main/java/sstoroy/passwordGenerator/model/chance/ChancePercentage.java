package sstoroy.passwordGenerator.model.chance;

record ChancePercentage(int chancePercentage) implements Chance {
    final static ChancePercentage NEVER = new ChancePercentage(0);
    final static ChancePercentage ALWAYS = new ChancePercentage(100);

    public ChancePercentage {
        if (chancePercentage < 0) {
            chancePercentage = 0;
        } else if (chancePercentage > 100) {
            chancePercentage = 100;
        }
    }

    @Override
    public boolean isPossible() {
        return chancePercentage() > 0;
    }

    @Override
    public boolean isAlways() {
        return chancePercentage() == 100;
    }

    @Override
    public int getPercentageAmount(int amount) {
        return (chancePercentage() * amount) / 100;
    }

    @Override
    public String toString() {
        return String.valueOf(chancePercentage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((!(o instanceof Chance that))) return false;
        return chancePercentage() == that.chancePercentage();
    }
}
