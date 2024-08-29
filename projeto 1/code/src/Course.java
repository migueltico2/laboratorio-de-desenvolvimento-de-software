import java.util.ArrayList;
public class Course {
    private static int counter = 1;
    private final int id;
    private String name;
    private int token;
    private ArrayList<Semester> semester;

    public Course(String name, int token) {
        this.id = counter++;
        this.name = name;
        this.token = token;
    }


    public void addSemester(Semester semester) {
        this.semester.add(semester);
    }

    public void removeSemester(Semester semester) {
        this.semester.remove(semester);
    }


    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getToken() {
        return this.token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public ArrayList<Semester> getSemester() {
        return this.semester;
    }
}
