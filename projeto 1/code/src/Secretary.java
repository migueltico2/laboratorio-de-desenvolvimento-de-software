import java.util.ArrayList;

import Enums.Status;

public class Secretary extends User {
    private static Database<Subject> subjectPersistence = new Database<>("Subjects", "subjects.dat");

    public Secretary(String name, String email, String password) {
        super(name, email, password);
    }

    public void allocateProfessor(Registry registry, Professor professor) {
    }

    public void dellocateProfessor(Registry registry, Professor professor) {
    }

    public void createCourse(Course course) {

    }

    public void deleteCourse(Course course) {
    }

    public void createSubject(Subject subject) {
        subjectPersistence.addItem(subject);
    }

    public void deleteSubject(Subject subject) {
    }

    public void updateSubjectStatus(Subject subject, Status status) {
    }

    public void generateCurriculum(Semester semester, ArrayList<Registry> registries) {
        Curriculum curriculum = new Curriculum("", registries);
        semester.setCurriculum(curriculum);
    }
}
