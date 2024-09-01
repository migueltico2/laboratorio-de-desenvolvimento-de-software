import java.util.Map;
import Enums.Status;

public class Secretary extends User {
    private static Database<Subject> subjectPersistence = new Database<>("Subjects", "subjects.dat");
    private static Database<Course> coursePersistence = new Database<>("Courses", "courses.dat");

    public Secretary(String name, String email, String password) {
        super(name, email, password);
    }

    public void allocateProfessor(Registry registry, Professor professor) {
        if (professor != null) return;
        registry.addProfessor(professor);   
    }

    public void dellocateProfessor(Registry registry, Professor professor) {
        if (professor != null) return;
        registry.removeProfessor(professor);
    }

    public void createCourse(Course course) {
        if (course != null) return;
        coursePersistence.addItem(course);
    }

    public void deleteCourse(Course course) {
        if (course != null) return;
        coursePersistence.deleteItem(course);
    }

    public void createSubject(Subject subject) {
        if (subject != null) return;
        subjectPersistence.addItem(subject);
    }

    public void deleteSubject(Subject subject) {
        if (subject != null) return;
        subjectPersistence.deleteItem(subject);
    }

    public void updateSubjectStatus(Registry registry, Status status) {
        registry.setStatus(status);
    }

    public void generateCurriculum(Semester semester, Map<String, Registry> registries) {
        Curriculum curriculum = new Curriculum(registries);
        semester.setCurriculum(curriculum);
    }
}
