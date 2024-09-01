import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Database<T> {
    private String fileName;
    private static final String DATABASE_FOLDER = "Database";
    private String subFolder;
    private List<T> items = new ArrayList<>();
    private Map<Class<?>, List<?>> childClasses;

    public Database(String subFolder, String fileName, Class<?>... childClasses) {
        this.subFolder = subFolder;
        this.fileName = DATABASE_FOLDER + File.separator + subFolder + File.separator + fileName;
        createDatabaseStructure();
        this.childClasses = new HashMap<>();
        for (Class<?> childClass : childClasses) {
            this.childClasses.put(childClass, new ArrayList<>());
        }
        loadFromFile();
    }

    private void createDatabaseStructure() {
        File directory = new File(DATABASE_FOLDER + File.separator + subFolder);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(items);
            for (Map.Entry<Class<?>, List<?>> entry : childClasses.entrySet()) {
                oos.writeObject(entry.getKey());
                oos.writeObject(entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(fileName);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                items = (List<T>) ois.readObject();
                while (true) {
                    Class<?> childClass = (Class<?>) ois.readObject();
                    List<?> childItems = (List<?>) ois.readObject();
                    childClasses.put(childClass, childItems);
                }
            } catch (IOException | ClassNotFoundException e) {
                // Reached the end of the file
            }
        }
    }

    public void addItem(T item) {
        items.add(item);
        saveToFile();
    }

    public void deleteItem(T item) {
        items.remove(item);
        saveToFile();
    }

    public List<T> getAllItems() {
        return items;
    }

    public T find(Predicate<T> func) {
        return items.stream()
                .filter(func)
                .findFirst()
                .get();
    }

    public List<T> filter(Predicate<T> func) {
        return items.stream()
                .filter(func)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <U> List<U> getChildItems(Class<U> childClass) {
        return (List<U>) childClasses.get(childClass);
    }
}