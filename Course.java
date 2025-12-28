
/**
 * Course Model
 * Represents a single course entity containing the course code, 
 * credit value, and the letter grade.
 * * @author Murat Can IŞIK
 */

import java.io.Serializable;

public class Course implements Serializable {
    private String code;
    private int credit;
    private LetterGrade letterGrade;

    public Course(String code, int credit, LetterGrade letterGrade) {
        this.code = code;
        this.credit = credit;
        this.letterGrade = letterGrade;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCredit() {
        return this.credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public LetterGrade getLetterGrade() {
        return this.letterGrade;
    }

    public void setLetterGrade(LetterGrade letterGrade) {
        this.letterGrade = letterGrade;
    }

}
