import java.util.ArrayList;

import Enums.Status;

public class Registry {
    private static int counter = 1;
    private final int id;
    private boolean required;
    private Status status;
    private final Subject subject;
    private ArrayList<Professor> professors;

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

    public ArrayList<Professor> getProfessors() {
        return this.professors;
    }

    public Professor findProfessor(String name) {
        return professors.stream()
        .filter(item -> item.getName().equals(name))
        .findFirst()
        .get();
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
