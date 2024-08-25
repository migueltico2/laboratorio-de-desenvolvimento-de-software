public abstract class User {
    private static int counter = 1;
    private final int id;
    private String name;
    private String email;
    private String password;

    public User() {
        id = counter++;
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

    public void setPassword(String oldPass, String newPass) {
    }

    public void login(String password) {}
}
