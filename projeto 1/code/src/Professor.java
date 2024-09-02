import java.util.Map;
import java.util.stream.Collectors;

public class Professor extends User {
    public Professor(String name, String email, String password) {
        super(name, email, password);
    }

    public Map<String, Registry> getRegistries(Database<Course> courseDatabase) {
        return courseDatabase.getAllItems().stream()
                .flatMap(item -> item.getSemester().stream()
                        .flatMap(semester -> semester.getCurriculum().getRegistry()
                                .stream().filter(r -> r.findProfessor(this.getName()) != null)))
                .collect(Collectors.toMap(
                        r -> r.getSubject().getName(),
                        r -> r));
    }

    public Map<String, Student> getRegistryStudents(Registry registry, Database<User> userDatabase) {
        return userDatabase.getAllItems().stream()
                .filter(item -> item instanceof Student)
                .map(item -> (Student) item)
                .filter(student -> registry.findEnrollment(student.getName()) != null)
                .collect(Collectors.toMap(
                        Student::getName,
                        student -> student));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
