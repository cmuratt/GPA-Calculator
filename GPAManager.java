
/**
 * GPA Manager
 * Manages the backend logic of the application.
 * Handles GPA/CGPA calculations, adding/removing semesters and courses,
 * and maintains the data structure in memory.
 * * @author Murat Can IŞIK
 */

import java.io.*;
import java.util.*;

public class GPAManager {
    private Map<String, ArrayList<Course>> semesterMap = new LinkedHashMap<>();
    private final String FILE_NAME = "gpa_data.bin";

    public GPAManager() {
        loadData();
    }

    public void addSemester(String name) {
        if (!semesterMap.containsKey(name)) {
            semesterMap.put(name, new ArrayList<>());
            saveData();
        }
    }

    public void removeSemester(String name) {
        if (semesterMap.containsKey(name)) {
            semesterMap.remove(name);
            saveData();
        }
    }

    public boolean renameSemester(String oldName, String newName) {
        if (semesterMap.containsKey(oldName) && !semesterMap.containsKey(newName)) {
            ArrayList<Course> courses = semesterMap.remove(oldName);
            semesterMap.put(newName, courses);
            saveData();
            return true;
        }
        return false;
    }

    public void addCourse(String semesterName, Course course) {
        if (semesterMap.containsKey(semesterName)) {
            semesterMap.get(semesterName).add(course);
            saveData();
        }
    }

    public void removeCourse(String semesterName, int index) {
        if (semesterMap.containsKey(semesterName)) {
            semesterMap.get(semesterName).remove(index);
            saveData();
        }
    }

    public double calculateSemesterGPA(String semesterName) {
        if (!semesterMap.containsKey(semesterName))
            return 0.0;
        return calculateGPAForList(semesterMap.get(semesterName));
    }

    public double calculateTotalCGPA() {
        double totalPoints = 0;
        int totalCredits = 0;
        for (ArrayList<Course> courses : semesterMap.values()) {
            for (Course c : courses) {
                totalPoints += (c.getCredit() * c.getLetterGrade().getValue());
                totalCredits += c.getCredit();
            }
        }
        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    public double calculateGPAForList(List<Course> courses) {
        if (courses == null || courses.isEmpty())
            return 0.0;
        double totalPoints = 0;
        int totalCredits = 0;
        for (Course course : courses) {
            totalPoints += (course.getCredit() * course.getLetterGrade().getValue());
            totalCredits += course.getCredit();
        }
        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    public Map<String, ArrayList<Course>> getAllSemesters() {
        return semesterMap;
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(semesterMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateCourse(String semesterName, int index, String newCode, int newCredit, LetterGrade newGrade) {
        if (semesterMap.containsKey(semesterName)) {
            ArrayList<Course> courses = semesterMap.get(semesterName);
            if (index >= 0 && index < courses.size()) {
                Course c = courses.get(index);
                c.setCode(newCode);
                c.setCredit(newCredit);
                c.setLetterGrade(newGrade);
                saveData();
            }
        }
    }

    public double calculateProjectedCGPA(double currentGPA, int currentTotalCredits, List<Course> newCourses) {
        double totalPoints = currentGPA * currentTotalCredits;
        int totalCredits = currentTotalCredits;

        for (Course c : newCourses) {
            totalPoints += (c.getCredit() * c.getLetterGrade().getValue());
            totalCredits += c.getCredit();
        }

        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                semesterMap = (Map<String, ArrayList<Course>>) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}