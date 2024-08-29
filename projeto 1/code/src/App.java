import java.util.Scanner;

public class App {
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


    private static void showInitialMenu(boolean isLogged){
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
    }else{
        showLoggedInMenu(user);
    }
    }

    private static void showLoginOptions(){
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

    private static void showLoginMenu(int option){
        // TODO: IMPLEMENT LOGIN LOGIC HERE
        User toLog = new Student("jose","email@email.com","123456");
        System.out.println("--------------------");
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        // TODO: SEARCH FOR EMAIL ON USER PERSISTENCE BEFORE COMPARING

        User logged = toLog.login(password);
        if (logged != null) {
            switch (option) {
                case 1:
                    isLoggedIn = true;
                    user = (Student) logged;
                    break;
                case 2:
                    isLoggedIn = true;
                    user = (Professor) logged;
                    break;
                case 3:
                    isLoggedIn = true;
                    user = (Secretary) logged;
                    break;
            }
        }else{
            System.out.println("Invalid email or password!");
        }
        
    }

    private static void showLoggedInMenu(User user){
        System.out.println("--------------------");
        if (user instanceof Student){
            showStudentOptions((Student) user);
        }else if (user instanceof Professor){
            showProfessorOptions((Professor) user);
        }else if (user instanceof Secretary){
            showSecretaryOptions((Secretary) user);
        }else{
            System.out.println("Invalid user type!");
        }
    }


    // REGISTER OPERATION METHODS
    private static void showRegisterOptions(){
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

    private static void showRegisterMenu(String registerType){
        System.out.println("--------------------");
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        switch (registerType) {
            case "student":
                Student student = new Student(name, email, password);
                System.out.println(student.toString());
                break;
            case "professor":
                Professor professor = new Professor(name, email, password);
                System.out.println(professor.toString());
                break;
            case "secretary":
                Secretary secretary = new Secretary(name, email, password);
                System.out.println(secretary.toString());
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    } 

    private static void showStudentOptions(Student user){
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

    private static void showProfessorOptions(Professor user){
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

    private static void showSecretaryOptions(Secretary user){
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

    private static int readOption(){
        String option = scanner.nextLine();
        return Integer.parseInt(option);
    }

}
