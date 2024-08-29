import java.util.ArrayList;

public class Curriculum {
    private static int counter = 1;
    private final int id;
    private String name;
    private ArrayList<Registry> registry;

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

    public ArrayList<Registry> getRegistry() {
        return this.registry;
    }

    public void addRegistry(Registry registry) {
        this.registry.add(registry);
    }

    public void removeRegistry(Registry registry) {
        this.registry.remove(registry);
    }

}
