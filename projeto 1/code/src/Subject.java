import java.io.Serializable;

public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    private static transient int counter = 1;
    private final int id;
    private String name;
    private int hours;
    private int token;

    public Subject(String name, int hours, int token) {
        this.id = counter++;
        this.name = name;
        this.hours = hours;
        this.token = token;
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

    public int getHours() {
        return this.hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getToken() {
        return this.token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Name: " + name + "- hours: " + hours;
    }
}
