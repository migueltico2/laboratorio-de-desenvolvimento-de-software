import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Database<T> {
    private String fileName;
    private static final String DATABASE_FOLDER = "Database";
    private String subFolder;
    private List<T> items;

    public Database(String subFolder, String fileName) {
        this.subFolder = subFolder;
        this.fileName = DATABASE_FOLDER + File.separator + subFolder + File.separator + fileName;
        createDatabaseStructure();
        items = loadFromFile();
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
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> loadFromFile() {
        List<T> items = new ArrayList<>();
        File file = new File(fileName);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                items = (List<T>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading from file: " + e.getMessage());
            }
        }
        return items;
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
}