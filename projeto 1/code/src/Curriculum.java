import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Curriculum implements Serializable {
    private static int counter = 1;
    private final int id;
    private Map<String, Registry> registry = new HashMap<>();

    public Curriculum() {
        this.id = counter++;
        init(new HashMap<>());
    }

    public Curriculum(Map<String, Registry> registries) {
        this.id = counter++;
        init(registries);
    }

    private void init(Map<String, Registry> registries) {
        this.registry = registries;
    }

    public int getId() {
        return this.id;
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
