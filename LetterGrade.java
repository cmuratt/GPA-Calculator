/**
 * LetterGrade Enum
 * Defines the standard letter grades and
 * their corresponding numerical coefficients.
 * * @author Murat Can IŞIK
 */

public enum LetterGrade {
    AA(4.0),
    BA(3.5),
    BB(3.0),
    CB(2.5),
    CC(2.0),
    DC(1.5),
    DD(1.0),
    FD(0.5),
    FF(0.0);

    private final double value;

    LetterGrade(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.name() + " / " + String.format("%.1f", this.value);
    }
}