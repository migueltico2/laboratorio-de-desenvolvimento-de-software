import java.util.Scanner;
import java.util.stream.Collectors;

import Enums.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    private static Database<User> userDatabase = new Database<>("Users", "users.dat", Student.class, Professor.class,
            Secretary.class);
    private static Database<Course> courseDatabase = new Database<>("Courses", "courses.dat", Semester.class,
            Curriculum.class, Registry.class);
    private static Database<Subject> subjectPersistence = new Database<>("Subjects", "subjects.dat");

    private static final Scanner scanner = new Scanner(System.in);
    private static Boolean isLoggedIn = false;
    private static Boolean exit = false;
    private static User user;

    public static void main(String[] args) throws Exception {
        while (!exit) {
            showInitialMenu(isLoggedIn);
        }
        scanner.close();
    }

    private static void showInitialMenu(boolean isLogged) {
        if (!isLogged) {
            logoutMenu();
        } else {
            showLoggedInMenu(user);
        }

        userDatabase.saveToFile();
        courseDatabase.saveToFile();
        subjectPersistence.saveToFile();
    }

    private static void logoutMenu() {
        System.out.println("--------------------");
        System.out.println("Welcome to the system!");
        System.out.println("1 - Login");
        System.out.println("2 - Register");
        System.out.println("3 - Exit");
        int option = readOption();
        switch (option) {
            case 1:
                showLoginMenu();
                break;
            case 2:
                showRegisterOptions();
                break;
            case 3:
                exit = true;
                System.out.println("Bye!");
                break;
        }
    }

    private static void showLoginMenu() {
        System.out.println("--------------------");
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        try {
            User toLog = userDatabase.find(item -> item.getEmail().equals(email));
            User logged = toLog.login(password);
            if (logged == null) {
                System.out.println("Invalid email or password!");
                return;
            }

            isLoggedIn = true;
            user = logged;
            System.out.println("Login successful!");
        } catch (Exception e) {
            System.out.println("User not found!");
        }
    }

    private static void showLoggedInMenu(User user) {
        System.out.println("--------------------");
        if (user instanceof Student) {
            showStudentOptions((Student) user);
        } else if (user instanceof Professor) {
            showProfessorOptions((Professor) user);
        } else if (user instanceof Secretary) {
            System.out.println("Secretary menu");
            showSecretaryOptions((Secretary) user);
        } else {
            System.out.println("Invalid user type!");
        }
    }

    // REGISTER OPERATION METHODS
    private static void showRegisterOptions() {
        System.out.println("--------------------");
        System.out.println("Register as: ");
        System.out.println("1 - Student");
        System.out.println("2 - Professor");
        System.out.println("3 - Secretary");
        System.out.println("Enter your option:");
        int option = readOption();
        switch (option) {
            case 1:
                System.out.println("Student register");
                showRegisterMenu("student");
                break;
            case 2:
                System.out.println("Professor register");
                showRegisterMenu("professor");
                break;
            case 3:
                System.out.println("Secretary register");
                showRegisterMenu("secretary");
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    private static void showRegisterMenu(String registerType) {
        System.out.println("--------------------");
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        User newUser = null;
        switch (registerType) {
            case "student":
                newUser = new Student(name, email, password);
                break;
            case "professor":
                newUser = new Professor(name, email, password);
                break;
            case "secretary":
                newUser = new Secretary(name, email, password);
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
        userDatabase.addItem(newUser);
        System.out.println("User registered successfully!");
        System.out.println(newUser.toString());
    }

    private static void showStudentOptions(Student user) {
        int option = 0;
        while (option != 6) {
            System.out.println("--------------------");
            System.out.println("1- Enter Course");
            System.out.println("2- Leave Course");
            System.out.println("3- List all courses");
            System.out.println("4- Enrollment options");
            System.out.println("5- List enrollments");
            System.out.println("6- Logout");
            option = readOption();
            Enrollment enrollment = null;
            Course course = null;
            String name = null;
            switch (option) {
                case 1:
                    try {
                        System.out.println("Enter the course name:");
                        String course_name = scanner.nextLine();
                        course = courseDatabase.find(item -> item.getName().equals(course_name));
                        enrollment = new Enrollment(course);
                        user.enroll(enrollment);
                        System.out.println("Enrolled successfully!");
                    } catch (Exception e) {
                        System.out.println("No course with this name");
                    }
                    break;
                case 2:
                    System.out.println("Enter the course name:");
                    name = scanner.nextLine();
                    enrollment = user.findEnrollment(name);
                    if (enrollment != null) {
                        user.unenroll(enrollment);
                    } else {
                        System.out.println("No course with this name");
                    }
                    break;
                case 3:
                    list(courseDatabase.getAllItems());
                    break;
                case 4:
                    System.out.println("Enter the course name:");
                    String courseName = scanner.nextLine();
                    try {
                        course = courseDatabase.find(item -> item.getName().equals(courseName));
                        enrollment = user.findEnrollment(courseName);
                        if (enrollment != null) {
                            enrollment.setCourse(course);
                            enrollmentOptions(user, enrollment);
                        } else {
                            System.out.println("You are not in this course");
                        }
                    } catch (Exception e) {
                        System.out.println("No course with this name");
                    }
                    break;
                case 5:
                    list(user.getEnrollments());
                    break;
                case 6:
                    isLoggedIn = false;
                    System.out.println("Bye!");
                    break;
            }
        }
    }

    private static void enrollmentOptions(Student user, Enrollment enrollment) {
        int option = 0;
        Map<String, Registry> courseRegistry = enrollment.getCourse().getSemester()
                .stream()
                .filter(semester -> semester.getPeriod() <= enrollment.getSemester())
                .flatMap(semester -> semester.getCurriculum().getRegistry().stream())
                .collect(Collectors.toMap(
                        r -> r.getSubject().getName(),
                        r -> r));

        while (option != 5) {
            Map<String, Registry> registries = enrollment.getCourse().getSemester()
                    .stream()
                    .filter(semester -> semester.getPeriod() <= enrollment.getSemester())
                    .flatMap(semester -> semester.getCurriculum().getRegistry().stream()
                            .filter(registry -> registry.findEnrollment(user.getName()) != null))
                    .collect(Collectors.toMap(
                            r -> r.getSubject().getName(),
                            r -> r));

            System.out.println("--------------------");
            System.out.println("1- Enroll");
            System.out.println("2- Unenroll");
            System.out.println("3- List Subjects");
            System.out.println("4- List All Subjects");
            System.out.println("5- Go back");
            option = readOption();

            switch (option) {
                case 1:
                    try {
                        System.out.println("Enter the subject name:");
                        String name = scanner.nextLine();
                        Registry registry = courseRegistry.get(name);
                        Registry oldRegistry = registries.get(name);

                        if (registry != null) {
                            if (oldRegistry == null) {
                                if (registry.getEnrollments().size() < 60) {
                                    if (registry.getRequired()) {
                                        int subjects = registries.size();
                                        if (subjects < 4) {
                                            registry.addEnrollment(user, enrollment);
                                            System.out.println("Enrolled successfully");
                                        } else {
                                            System.out.println("You are already 4 required subject");
                                        }
                                    } else {
                                        int subjects = registries.size();
                                        if (subjects < 2) {
                                            registry.addEnrollment(user, enrollment);
                                            courseDatabase.saveToFile();
                                            System.out.println("Enrolled successfully");
                                        } else {
                                            System.out.println("You are already 2 optionals subject");
                                        }
                                    }
                                } else {
                                    System.out.println("Subject not available");
                                }
                            } else {
                                System.out.println("You are already enrolled in this subject");
                            }
                        } else {
                            System.out.println("No subject with this name");
                        }
                    } catch (Exception e) {
                        System.out.println("No subject with this name");
                    }
                    break;
                case 2:
                    System.out.println("Enter the subject name:");
                    String name = scanner.nextLine();
                    Registry registry = registries.get(name);

                    if (registry != null) {
                        registry.removeEnrollment(user);
                        System.out.println("Unenrolled successfully");
                    } else {
                        System.out.println("No subject with this name");
                    }
                    break;
                case 3:
                    list(new ArrayList<>(registries.values()));
                case 4:
                    list(new ArrayList<>(courseRegistry.values()));
                    break;
            }
        }
    }

    private static void showProfessorOptions(Professor user) {
        int option = 0;
        Map<String, Registry> registries = courseDatabase.getAllItems().stream()
                .flatMap(item -> item.getSemester().stream()
                        .flatMap(semester -> semester.getCurriculum().getRegistry()
                                .stream().filter(r -> r.findProfessor(user.getName()).equals(user))))
                .collect(Collectors.toMap(
                        r -> r.getSubject().getName(),
                        r -> r));

        while (option != 3) {
            System.out.println("--------------------");
            System.out.println("1- Get Subject");
            System.out.println("2- List Subjects");
            System.out.println("3- Logout");
            option = readOption();
            switch (option) {
                case 1:
                    try {
                        System.out.println("Enter the subject name:");
                        String name = scanner.nextLine();
                        registryOptionsProf(registries.get(name));
                    } catch (Exception e) {
                        System.out.println("No subject with this name");
                    }
                    break;
                case 2:
                    list(new ArrayList<>(registries.values()));
                    break;
                case 3:
                    isLoggedIn = false;
                    System.out.println("Bye!");
                    break;
            }
        }
    }

    private static void registryOptionsProf(Registry registry) {
        int option = 0;
        Map<String, Student> students = userDatabase.getAllItems().stream()
                .filter(item -> item instanceof Student)
                .map(item -> (Student) item)
                .filter(student -> student.getEnrollments().stream()
                        .anyMatch(enrollment -> registry.getEnrollments().contains(enrollment)))
                .collect(Collectors.toMap(
                        Student::getName,
                        student -> student));
        while (option != 2) {
            System.out.println("--------------------");
            System.out.println("1- Get Student");
            System.out.println("2- List Students");
            System.out.println("3- Go back");
            option = readOption();
            switch (option) {
                case 1:
                    try {
                        System.out.println("Enter the student name:");
                        String name = scanner.nextLine();
                        students.get(name);
                    } catch (Exception e) {
                        System.out.println("No student with this name");
                    }
                    break;
                case 2:
                    list(new ArrayList<>(students.values()));
                    break;
            }
        }
    }

    private static void showSecretaryOptions(Secretary user) {
        int option = 0;
        while (option != 5) {
            System.out.println("--------------------");
            System.out.println("1- Create subject");
            System.out.println("2- Get subject by name");
            System.out.println("3- Get all subjects");
            System.out.println("4- Course Options");
            System.out.println("5- Logout");
            option = readOption();

            switch (option) {
                case 1:
                    createSubjectMenu();
                    break;
                case 2:
                    System.out.println("Enter the subject name:");
                    String name = scanner.nextLine();
                    try {
                        list(subjectPersistence
                                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase())));
                    } catch (Exception e) {
                        System.out.println("No subject with this name");
                    }
                    break;
                case 3:
                    list(subjectPersistence.getAllItems());
                    break;
                case 4:
                    courseOperations(user);
                    break;
                case 5:
                    isLoggedIn = false;
                    System.out.println("Bye!");
                    break;
            }
        }
    }

    private static void createSubjectMenu() {
        System.out.println("--------------------");
        System.out.println("Enter the subject name:");
        String name = scanner.nextLine();
        System.out.println("Enter the subject hours:");
        int hours = readOption();
        if (hours == 0) {
            System.out.println("Error to create subject");
            return;
        }
        System.out.println("Enter the subject token:");
        int token = readOption();
        if (token == 0) {
            System.out.println("Error to create subject");
            return;
        }

        Subject subject = new Subject(name, hours, token);
        ((Secretary) user).createSubject(subject);
        subjectPersistence.addItem(subject);
        System.out.println("Subject created successfully!");
        System.out.println(subject.toString());
    }

    private static void courseOperations(Secretary user) {
        int option = 0;
        Course course = null;

        while (option != 5) {
            System.out.println("--------------------");
            System.out.println("1- Create course");
            System.out.println("2- Delete course");
            System.out.println("3- List courses");
            System.out.println("4- Semester Options");
            System.out.println("5- Go back");
            option = readOption();

            switch (option) {
                case 1:
                    System.out.println("Enter the course name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter the course token:");
                    int token = readOption();
                    course = new Course(name, token);
                    courseDatabase.addItem(course);
                    System.out.println("Course created successfully!");
                    System.out.println(course.toString());
                    break;
                case 2:
                    break;
                case 3:
                    list(courseDatabase.getAllItems());
                    break;
                case 4:
                    course = getCourse();
                    if (course != null)
                        semesterOperations(user, course);
                    break;
            }
        }
    }

    private static void semesterOperations(Secretary user, Course course) {
        int option = 0;
        Semester semester = null;

        while (option != 5) {
            System.out.println("--------------------");
            System.out.println("1- Add semester");
            System.out.println("2- Delete semester");
            System.out.println("3- Number of Semesters");
            System.out.println("4- Curriculum Options");
            System.out.println("5- Go back");
            option = readOption();
            switch (option) {
                case 1:
                    semester = createSemester(user, course);
                    if (semester != null) {
                        course.addSemester(semester);
                        System.out.println("Semester added successfully");
                    }
                    break;
                case 2:
                    semester = getSemestre(course);
                    if (semester != null)
                        course.removeSemester(semester);
                    break;
                case 3:
                    System.out.println("This course have " + course.getSemester().size() + " semesters");
                    break;
                case 4:
                    semester = getSemestre(course);
                    if (semester != null)
                        curriculumOperations(user, semester);
                    break;
            }
        }
    }

    private static void curriculumOperations(Secretary user, Semester semester) {
        int option = 0;
        while (option != 3) {
            System.out.println("--------------------");
            System.out.println("1- Generate Curriculum");
            System.out.println("2- Registry Options");
            System.out.println("3- Go back");
            option = readOption();
            switch (option) {
                case 1:
                    generateCurriculum(user, semester);
                    System.out.println("Curriculum generated successfully");
                    break;
                case 2:
                    registryOperations(user, semester.getCurriculum());
                    break;
            }
        }
    }

    private static void registryOperations(Secretary user, Curriculum curriculum) {
        int option = 0;
        Registry registry = null;

        while (option != 6) {
            System.out.println("--------------------");
            System.out.println("1- Add Registry");
            System.out.println("2- Remove Registry");
            System.out.println("3- Change Status");
            System.out.println("4- List Registries");
            System.out.println("5- Professor Options");
            System.out.println("6- Go back");
            option = readOption();
            switch (option) {
                case 1:
                    registry = createRegistry();
                    if (registry != null) {
                        curriculum.addRegistry(registry);
                        System.out.println("Registry added successfully");
                    }
                    break;
                case 2:
                    registry = getRegistry(curriculum);
                    if (registry != null)
                        curriculum.removeRegistry(registry);
                    break;
                case 3:
                    registry = getRegistry(curriculum);
                    if (registry != null)
                        statusOptions(registry);
                    break;
                case 4:
                    list(curriculum.getRegistry());
                    break;
                case 5:
                    registry = getRegistry(curriculum);
                    if (registry != null)
                        professorOptions(registry);
                    break;
            }
        }
    }

    private static void statusOptions(Registry registry) {
        System.out.println("--------------------");
        System.out.println("1- Available");
        System.out.println("2- Full");
        System.out.println("3- Cancelled");
        int option = readOption();
        switch (option) {
            case 1:
                registry.setStatus(Status.AVAILABLE);
                break;
            case 2:
                registry.setStatus(Status.FULL);
                break;
            case 3:
                registry.setStatus(Status.CANCELLED);
                break;
            default:
                System.out.println("No such status");
                break;
        }
    }

    private static void professorOptions(Registry registry) {
        int option = 0;
        Professor prof = null;

        while (option != 5) {
            System.out.println("--------------------");
            System.out.println("1- Add Professor");
            System.out.println("2- Remove Professor");
            System.out.println("3- List Professors");
            System.out.println("4- List All Professors");
            System.out.println("5- Go back");
            option = readOption();
            switch (option) {
                case 1:
                    try {
                        System.out.println("Enter the course name:");
                        String name = scanner.nextLine();
                        prof = (Professor) userDatabase
                                .find(item -> item instanceof Professor && item.getName().equals(name));
                        registry.addProfessor(prof);
                        System.out.println("Professor added successfully");
                    } catch (Exception e) {
                        System.out.println("No professor with this name");
                    }
                    break;
                case 2:
                    prof = getProfessor(registry);
                    if (prof != null)
                        registry.removeProfessor(prof);
                    break;
                case 3:
                    list(registry.getProfessors());
                    break;
                case 4:
                    list(userDatabase.filter(item -> item instanceof Professor));
                    break;
            }
        }
    }

    private static Course getCourse() {
        Course course = null;
        try {
            System.out.println("Enter the course name:");
            String name = scanner.nextLine();
            course = courseDatabase.find(item -> item.getName().equals(name));
        } catch (Exception e) {
            System.out.println("No course with this name");
        }

        return course;
    }

    private static Semester getSemestre(Course course) {
        Semester semester = null;
        System.out.println("Enter the semester period:");
        int period = readOption();
        if (period == 0)
            return null;
        semester = course.findSemester(period);
        if (semester == null) {
            System.out.println("No such period");
        }
        return semester;
    }

    private static Semester createSemester(Secretary user, Course course) {
        Semester semester = null;
        System.out.println("Enter the semester period:");
        int period = readOption();
        if (period == 0)
            return null;
        semester = course.findSemester(period);
        if (semester != null) {
            System.out.println("This period already exists");
        } else {
            semester = new Semester(period, null);
            generateCurriculum(user, semester);
        }

        return semester;
    }

    private static void generateCurriculum(Secretary user, Semester semester) {
        Map<String, Registry> registries = new HashMap<>();
        int option = 0;

        while (option != 3) {
            System.out.println("--------------------");
            System.out.println("1- Add Subject");
            System.out.println("2- List Subjects");
            System.out.println("3- End Generation");
            option = readOption();

            switch (option) {
                case 1:
                    Registry registry = createRegistry();
                    if (registry != null) {
                        registries.put(registry.getSubject().getName(), registry);
                        System.out.println("Registry added successfully");
                    }
                    break;
                case 2:
                    list(subjectPersistence.getAllItems());
                    break;
            }
        }

        user.generateCurriculum(semester, registries);
    }

    private static Registry getRegistry(Curriculum curriculum) {
        Registry registry = null;
        if (curriculum != null) {
            System.out.println("Enter the registry subject name:");
            String name = scanner.nextLine();
            registry = curriculum.findRegistry(name);
        } else {
            System.out.println("No registry subject with this name");
        }

        return registry;
    }

    private static Registry createRegistry() {
        Registry registry = null;

        try {
            System.out.println("Enter the subject name:");
            String name = scanner.nextLine();
            Subject subject = subjectPersistence.find(item -> item.getName().toLowerCase().equals(name.toLowerCase()));
            System.out.println("Is this subject required? (YES/NO)");
            boolean required = scanner.nextLine().equals("YES");
            registry = new Registry(required, Status.AVAILABLE, subject);
        } catch (Exception e) {
            System.out.println("No subject with this name");
        }

        return registry;
    }

    private static Professor getProfessor(Registry registry) {
        Professor professor = null;
        System.out.println("Enter the professor name:");
        String name = scanner.nextLine();
        professor = registry.findProfessor(name);
        if (professor != null) {
            System.out.println("No professor with this name");
        }

        return professor;
    }

    private static <T> void list(List<T> arr) {
        arr.stream().forEach(item -> System.out.println(item.toString()));
    }

    private static int readOption() {
        try {
            String option = scanner.nextLine();
            return Integer.parseInt(option);
        } catch (Exception e) {
            System.out.println("Invalid number");
        }

        return 0;
    }

}
