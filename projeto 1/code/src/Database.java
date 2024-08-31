import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Database<T> {
    private String fileName;
    private static final String DATABASE_FOLDER = "Database";
    private String subFolder;

    public Database(String subFolder, String fileName) {
        this.subFolder = subFolder;
        this.fileName = DATABASE_FOLDER + File.separator + subFolder + File.separator + fileName;
        createDatabaseStructure();
    }

    private void createDatabaseStructure() {
        File directory = new File(DATABASE_FOLDER + File.separator + subFolder);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void saveToFile(List<T> items) {
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
        List<T> items = loadFromFile();
        items.add(item);
        saveToFile(items);
    }

    public List<T> getAllItems() {
        return loadFromFile();
    }

    public T findItemByEmail(String email) {
        List<T> items = loadFromFile();
        for (T item : items) {
            if (item instanceof User && ((User) item).getEmail().equals(email)) {
                return item;
            }
        }
        return null;
    }

    // Add a method to find a subject by its code
    public T findSubjectByCode(String code) {
        List<T> items = loadFromFile();
        for (T item : items) {
            if (item instanceof Subject && ((Subject) item).getId() == Integer.parseInt(code)) {
                return item;
            }
        }
        return null;
    }

    public List<T> getAllSubjects() {
        return loadFromFile().stream()
                .filter(item -> item instanceof Subject)
                .collect(Collectors.toList());
    }

    public List<T> findSubjectsByName(String name) {
        List<T> items = loadFromFile();
        return items.stream()
                .filter(item -> item instanceof Subject && ((Subject) item).getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }



}