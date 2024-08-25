public abstract class User {
    private static int counter = 1;
    private final int id;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        id = counter++;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void login(String password) {}

    public void changePassword(String oldPass, String newPass) {}
}
