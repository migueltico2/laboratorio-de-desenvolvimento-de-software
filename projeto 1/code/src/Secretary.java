import java.util.Map;
import Enums.Status;

public class Secretary extends User {
    public Secretary(String name, String email, String password) {
        super(name, email, password);
    }

    public void allocateProfessor(Registry registry, Professor professor) {
        if (professor == null)
            return;
        registry.addProfessor(professor);
    }

    public void dellocateProfessor(Registry registry, Professor professor) {
        if (professor == null)
            return;
        registry.removeProfessor(professor);
        System.out.println("Professor removed successfully");
    }

    public void createCourse(Course course, Database<Course> courseDatabase) {
        if (course == null)
            return;
        try {
            courseDatabase.find(item -> item.getName().equals(course.getName()));
            System.out.println("Course already exists");
        } catch (Exception e) {
            courseDatabase.addItem(course);
            System.out.println("Course created successfully!");
        }
    }

    public void deleteCourse(Course course, Database<Course> courseDatabase) {
        if (course == null)
            return;
        courseDatabase.deleteItem(course);
        System.out.println("Course removed successfully");
    }

    public void createSubject(Subject subject, Database<Subject> subjectPersistence) {
        if (subject == null)
            return;
        try {
            subjectPersistence.find(item -> item.getName().equals(subject.getName()));
            System.out.println("Subject already exists");
        } catch (Exception e) {
            subjectPersistence.addItem(subject);
            System.out.println("Subject created successfully!");
        }
    }

    public void deleteSubject(Subject subject, Database<Subject> subjectPersistence) {
        if (subject == null)
            return;
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
