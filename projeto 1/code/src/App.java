import java.util.Scanner;

import Enums.Status;

import java.util.ArrayList;

public class App {
    private static Database<User> userPersistence = new Database<>("Users", "users.dat");
    private static Database<Subject> subjectPersistence = new Database<>("Subjects", "subjects.dat");
    private static Database<Course> coursePersistence = new Database<>("Courses", "courses.dat");
    private static Database<Semester> semesterPersistence = new Database<>("Semesters", "semesters.dat");
    private static Database<Curriculum> curriculumsPersistence = new Database<>("Curriculums", "curriculums.dat");
    private static Database<Registry> registriesPersistence = new Database<>("Registries", "registries.dat");

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
    }

    private static void logoutMenu() {
        System.out.println("--------------------");
        System.out.println("Welcome to the system!");
        System.out.println("1 - Login");
        System.out.println("2 - Register");
        System.out.println("3 - Exit");
        System.out.println("Enter your option:");
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
            default:
                System.out.println("Invalid option!");
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
            User toLog = userPersistence.find(item -> item.getEmail().equals(email));

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
        userPersistence.addItem(newUser);
        System.out.println("User registered successfully!");
        System.out.println(newUser.toString());
    }

    private static void showStudentOptions(Student user) {
        System.out.println("--------------------");
        System.out.println("1- Get enrollments");
        System.out.println("2- Logout");
        int option = readOption();
        switch (option) {
            case 1:
                System.out.println("Enrollments: " + user.getEnrollments());
                break;
            case 2:
                isLoggedIn = false;
                System.out.println("Bye!");
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    private static void showProfessorOptions(Professor user) {
        System.out.println("--------------------");
        System.out.println("1- Get courses");
        System.out.println("2- Logout");
        int option = readOption();
        switch (option) {
            case 1:
                System.out.println("Courses:");
                break;
            case 2:
                isLoggedIn = false;
                System.out.println("Bye!");
                break;
            default:
                System.out.println("Invalid option!");
                break;
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
                        System.out.println(subjectPersistence
                                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase())).toString());
                    } catch (Exception e) {
                        System.out.println("No subject with this name");
                    }
                    break;
                case 3:
                    System.out.println(subjectPersistence.getAllItems().toString());
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
        System.out.println("Enter the subject token:");
        int token = readOption();
        Subject subject = new Subject(name, hours, token);
        ((Secretary) user).createSubject(subject);
        System.out.println("Subject created successfully!");
        System.out.println(subject.toString());
    }

    private static void courseOperations(Secretary user) {
        int option = 0;
        while (option != 4) {
            System.out.println("--------------------");
            System.out.println("1- Create course");
            System.out.println("1- Edit course");
            System.out.println("2- Delete course");
            System.out.println("3- Semester Options");
            System.out.println("4- Go back");
            option = readOption();
            switch (option) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    System.out.println("Enter the semester period:");
                    int period = readOption();
                    // System.out.println(subjectPersistence.find(item -> ((Subject)
                    // item).getName().toLowerCase().contains(name.toLowerCase())).toString());
                    semesterOperations(user, null);
                    break;
            }
        }
    }

    private static void semesterOperations(Secretary user, Course course) {
        int option = 0;
        while (option != 4) {
            System.out.println("--------------------");
            System.out.println("1- Create semester");
            System.out.println("1- Edit semester");
            System.out.println("2- Delete semester");
            System.out.println("3- Curriculum Options");
            System.out.println("4- Go back");
            option = readOption();
            switch (option) {
                case 1:
                    break;
                case 2:
                    System.out.println("Enter the semester period:");
                    int period = readOption();
                    Semester semester = course.getSemester().get(period - 1);
                    curriculumOperations(user, semester);
                    break;
                case 3:
                    System.out.println(subjectPersistence.getAllItems().toString());
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
                    break;
                case 2:
                    registryOperations(user, semester.getCurriculum());
                    break;
            }
        }
    }

    private static void generateCurriculum(Secretary user, Semester semester) {
        System.out.println("--------------------");
        ArrayList<Registry> registries = new ArrayList<>();
        String value = "";

        while (!value.equals("END")) {
            System.out.println("Enter END to finalize:");
            value = scanner.nextLine();

            Registry registry = createRegistry();
            if (registry != null) registries.add(registry);
        }

        user.generateCurriculum(semester, registries);
    }

    private static void registryOperations(Secretary user, Curriculum curriculum) {
        int option = 0;
        Registry registry = null;

        while (option != 6) {
            System.out.println("--------------------");
            System.out.println("1- Add Registry");
            System.out.println("2- Remove Registry");
            System.out.println("3- Add Professor");
            System.out.println("4- Remove Professor");
            System.out.println("5- Change Status");
            System.out.println("6- Go back");
            option = readOption();
            switch (option) {
                case 1:
                    registry = createRegistry();
                    if (registry != null) curriculum.addRegistry(registry);
                    break;
                case 2:
                    registry = getRegsitry(curriculum);
                    if (registry != null) registriesPersistence.deleteItem(registry);
                    break;
                case 3:
                    registry = getRegsitry(curriculum);
                    if (registry != null) {}
                    break;
            }
        }
    }

    private static Registry getRegsitry(Curriculum curriculum) {
        Registry registry = null;
        try {
            System.out.println("Enter the registry subject name:");
            String name = scanner.nextLine();
            registry = curriculum.findRegistry(name);
        } catch (Exception e) {
            System.out.println("No registry subject with this name");
        }

        return registry;
    }

    private static Registry createRegistry() {
        Registry registry =  null;

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

    private static void getProfessor(Registry registry) {
        // Professor professor = null;
        // try {
        //     System.out.println("Enter the registry subject name:");
        //     String name = scanner.nextLine();
        //     registry = curriculum.findRegistry(name);
        // } catch (Exception e) {
        //     System.out.println("No registry subject with this name");
        // }

        // return registry;
    }

    private static int readOption() {
        String option = scanner.nextLine();
        return Integer.parseInt(option);
    }

}
