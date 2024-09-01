public class Professor extends User {
    public Professor(String name, String email, String password) {
        super(name, email, password);
    }

    public Subject[] getSubjects() {
        return null;
    }

    public Student[] getSubjectStudents(Subject subject) {
        return null;
    }


    public void addRegistry(Registry registry) {
    }

    public void deleteRegistry(Registry registry) {
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
