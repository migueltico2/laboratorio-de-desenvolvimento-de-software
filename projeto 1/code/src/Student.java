public class Student extends User {
    private Enrollment[] enrollments;

    public Student(String name, String email, String password) {
        super(name, email, password);
    }

    public void enroll(Enrollment enrollment) {
    }

    public void unenroll(Enrollment enrollment) {
    }

    public Enrollment[] getEnrollments() {
        return enrollments;
    }
}
