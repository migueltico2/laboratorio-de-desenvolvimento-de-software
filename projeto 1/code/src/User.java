import java.io.Serializable;

public abstract class User implements Serializable {
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

    public User login(String password) {
        if (this.password.equals(password)) {
            return this;
        } else {
            return null;
        }
    }

    public void changePassword(String oldPass, String newPass) {
        if (this.password.equals(oldPass)) {
            this.password = newPass;
            System.out.println("Senha alterada com sucesso!");
        } else {
            System.out.println("Senha incorreta!");
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
