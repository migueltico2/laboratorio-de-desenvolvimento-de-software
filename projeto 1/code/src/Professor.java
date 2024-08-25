public class Professor extends User {
    private Registry[] registries;

    public Professor(String name, String email, String password) {
        super(name, email, password);
    }

    public Subject[] getSubjects() {
        return null;
    }

    public Student[] getSubjectStudents(Subject subject) {
        return null;
    }

    public Registry[] getRegistries() {
        return this.registries;
    }

    public void addRegistry(Registry registry) {
    }

    public void deleteRegistry(Registry registry) {
    }
}
