import java.util.ArrayList;

public class Student extends User {
    private ArrayList<Enrollment> enrollments;

    public Student(String name, String email, String password) {
        super(name, email, password);
        enrollments = new ArrayList<>();
    }

    public void enroll(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }

    public void unenroll(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
    }

    public ArrayList<Enrollment> getEnrollments() {
        return enrollments;
    }
}
