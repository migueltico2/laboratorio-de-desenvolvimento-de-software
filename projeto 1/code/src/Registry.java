import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Enums.Status;

public class Registry {
    private static int counter = 1;
    private final int id;
    private boolean required;
    private Status status;
    private final Subject subject;
    private Map<String, Professor> professors = new HashMap<>();

    public Registry(boolean required, Status status, Subject subject) {
        this.id = counter++;
        this.required = required;
        this.status = status;
        this.subject = subject;
    }


    public Subject getSubject() {
        return subject;
    }

    public Status getStatus() {
        return status;
    }


    public int getId() {
        return this.id;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean getRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Professor> getProfessors() {
        return this.professors.values().stream().collect(Collectors.toList());
    }

    public Professor findProfessor(String name) {
        return professors.get(name);
    }

    public void addProfessor(Professor professor) {
    }

    public void removeProfessor(Professor professor) {
    }

    public void addEnrollment(Enrollment enrollment) {
    }

    public void removeEnrollment(Enrollment enrollment) {
    }

    public void updateStatus(Status status) {
    }
}
