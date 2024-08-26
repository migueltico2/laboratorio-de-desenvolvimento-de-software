public class Enrollment {
    private static int counter = 1;
    private final int id;
    private int semester = 1;
    private final Student student;
    private Course course;
    private Registry[] registries;

    public Enrollment(Student student, Course course) {
        this.id = counter++;
        this.student = student;
        this.course = course;
    }

    public int getId() {
        return this.id;
    }

    public int getSemester() {
        return this.semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Student getStudent() {
        return this.student;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Registry[] getRegistries() {
        return this.registries;
    }

    public void addRegistry(Registry registry) {
    }

    public void deleteRegistry(Registry registry) {
    }


}
