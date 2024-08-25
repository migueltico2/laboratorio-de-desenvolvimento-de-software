import Enums.Status;

public class Registry {
    int id;
    Boolean required;
    Status status;
    Subject subject;
    Student[] students;
    Professor[] professors;

    public Subject getSubject() {
        return null;
    }

    public Status getStatus() {
        return null;
    }

    public void addProfessor(Professor professor) {
    }

    public void removeProfessor(Professor professor) {
    }

    public void addStudent(Student student) {
    }

    public void removeStudent(Student student) {
    }

    public void updateStatus(Status status) {
    }
}
