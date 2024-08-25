public class Student extends User {
    Enrollment[] enrollments;

    private boolean enroll(Enrollment enrollment) {
        return true;
    }

    private boolean unenroll(Enrollment enrollment) {
        return true;
    }
}
