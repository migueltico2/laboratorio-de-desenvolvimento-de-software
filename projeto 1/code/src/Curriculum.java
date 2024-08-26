public class Curriculum {
    private static int counter = 1;
    private final int id;
    private String name;
    private Registry[] registry;

    public Curriculum(String name) {
        this.id = counter++;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Registry[] getRegistry() {
        return this.registry;
    }

    public void addRegistry(Registry registry) {
    }

    public void removeRegistry(Registry registry) {
    }

}
