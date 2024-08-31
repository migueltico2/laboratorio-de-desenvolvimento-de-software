import java.util.Scanner;
import java.util.List;
public class App {
    private static Database<User> userPersistence = new Database<>("Users", "users.dat");
    private static Database<Subject> subjectPersistence = new Database<>("Subjects", "subjects.dat");

    private static final Scanner scanner = new Scanner(System.in);
    private static Boolean isLoggedIn = false;
    private static Boolean exit = false;
    private static User user;

    public static void main(String[] args) throws Exception {
        while (!exit) {
            if (!isLoggedIn) {
                showInitialMenu(false);
            } else {
                showInitialMenu(true);
            }
        }
        scanner.close();
    }

    private static void showInitialMenu(boolean isLogged) {
        if (!isLogged) {
            System.out.println("--------------------");
            System.out.println("Welcome to the system!");
            System.out.println("1 - Login");
            System.out.println("2 - Register");
            System.out.println("3 - Exit");
            System.out.println("Enter your option:");
            int option = readOption();
            switch (option) {
                case 1:
                    showLoginOptions();
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
        } else {
            showLoggedInMenu(user);
        }
    }

    private static void showLoginOptions() {
        System.out.println("--------------------");
        System.out.println("Login as: ");
        System.out.println("1 - Student");
        System.out.println("2 - Professor");
        System.out.println("3 - Secretary");
        System.out.println("Enter your option:");
        int option = readOption();
        switch (option) {
            case 1:
                System.out.println("Student login");
                showLoginMenu(option);
                break;
            case 2:
                System.out.println("Professor login");
                showLoginMenu(option);
                break;
            case 3:
                System.out.println("Secretary login");
                showLoginMenu(option);
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    private static void showLoginMenu(int option) {
        System.out.println("--------------------");
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        User toLog = userPersistence.findItemByEmail(email);
        if (toLog == null) {
            System.out.println("User not found!");
            return;
        }

        User logged = toLog.login(password);
        if (logged == null) {
            System.out.println("Invalid email or password!");
            return;
        }

        if (!isValidUserType(logged, option)) {
            System.out.println("Invalid user type for this login option!");
            return;
        }

        isLoggedIn = true;
        user = logged;
        System.out.println("Login successful!");
    }

    private static boolean isValidUserType(User user, int option) {
        return (option == 1 && user instanceof Student) ||
                (option == 2 && user instanceof Professor) ||
                (option == 3 && user instanceof Secretary);
    }

    private static void showLoggedInMenu(User user) {
        System.out.println("--------------------");
        System.out.println(user instanceof Secretary);
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
        System.out.println("--------------------");
        System.out.println("1- Create subject");
        System.out.println("2- Get subject by name");
        System.out.println("3- Get all subjects");
        System.out.println("5- Logout");
        int option = readOption();
        switch (option) {
            case 1:
                createSubjectMenu();
                break;
            case 2:
                System.out.println("Enter the subject name:");
                String name = scanner.nextLine();
                System.out.println(subjectPersistence.findSubjectsByName(name).toString());
                break;
            case 3:
                System.out.println(subjectPersistence.getAllSubjects().toString());
                break;
            case 5: 
                isLoggedIn = false;
                System.out.println("Bye!");
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    private static void createSubjectMenu(){
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

    private static int readOption() {
        String option = scanner.nextLine();
        return Integer.parseInt(option);
    }

}
