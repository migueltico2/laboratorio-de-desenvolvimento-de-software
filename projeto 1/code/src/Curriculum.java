import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Curriculum implements Serializable {
    private static int counter = 1;
    private final int id;
    private String name;
    private Map<String, Registry> registry = new HashMap<>();

    public Curriculum(String name) {
        this.id = counter++;
        init(name, new HashMap<>());
    }

    public Curriculum(String name, Map<String, Registry> registries) {
        this.id = counter++;
        init(name, registries);
    }

    private void init(String name, Map<String, Registry> registries) {
        this.name = name;
        this.registry = registries;
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

    public List<Registry> getRegistry() {
        return this.registry.values().stream().collect(Collectors.toList());
    }

    public Registry findRegistry(String name) {
        return registry.get(name);
    }

    public void addRegistry(Registry registry) {
        this.registry.put(registry.getSubject().getName(), registry);
    }

    public void removeRegistry(Registry registry) {
        this.registry.remove(registry.getSubject().getName());
    }

}
