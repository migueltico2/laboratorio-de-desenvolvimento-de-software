import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Course implements Serializable {
    private static int counter = 1;
    private final int id;
    private String name;
    private int token;
    private Map<Integer, Semester> semester = new HashMap<>();

    public Course(String name, int token) {
        this.id = counter++;
        this.name = name;
        this.token = token;
    }

    public void addSemester(Semester semester) {
        this.semester.put(semester.getPeriod(), semester);
    }

    public void removeSemester(Semester semester) {
        this.semester.remove(semester.getPeriod());
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

    public List<Semester> getSemester() {
        return this.semester.values().stream().collect(Collectors.toList());
    }

    public Semester findSemester(int period) {
        return this.semester.get(period);
    }
}
