import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Student extends User {
    private Map<String, Enrollment> enrollments = new HashMap<>();

    public Student(String name, String email, String password) {
        super(name, email, password);
    }

    public void enroll(Enrollment enrollment) {
        this.enrollments.put(enrollment.getCourse().getName(), enrollment);
    }

    public void unenroll(Enrollment enrollment) {
        this.enrollments.remove(enrollment.getCourse().getName());
    }

    public Enrollment findEnrollment(String name) {
        return enrollments.get(name);
    }

    public List<Enrollment> getEnrollments() {
        return enrollments.values().stream().collect(Collectors.toList());
    }

    public Map<String, Registry> getStudentRegisties(Enrollment enrollment) {
        return enrollment.getEnrollmentRegisties()
                            .filter(registry -> registry.findEnrollment(this.getName()) != null)
                    .collect(Collectors.toMap(
                            r -> r.getSubject().getName(),
                            r -> r));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
