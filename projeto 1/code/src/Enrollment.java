public class Enrollment {
    int id;
    int semester;
    Student student;
    Course course;
    Registry[] registries;


    public Enrollment(int id, int semester, Student student, Course course, Registry[] registries) {
        this.id = id;
        this.semester = semester;
        this.student = student;
        this.course = course;
        this.registries = registries;
    }


    public void addRegistry(Registry registry) {
    }

    public void deleteRegistry(Registry registry) {
    }

}
