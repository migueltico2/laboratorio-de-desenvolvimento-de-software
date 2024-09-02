import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Enrollment implements Serializable {
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

    public void setCourse(Course course) {
        this.course = course;
    }

    public Stream<Registry> getEnrollmentRegisties() {
        return this.getCourse().getCourseRegisties(this.getSemester());
    }

    @Override
    public String toString() {
        return course.toString();
    }
}
