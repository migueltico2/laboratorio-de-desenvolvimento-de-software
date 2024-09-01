public class Enrollment {
    private static int counter = 1;
    private final int id;
    private int semester = 1;
    private Course course;

    public Enrollment(Course course) {
        this.id = counter++;
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

    public Course getCourse() {
        return this.course;
    }
}
