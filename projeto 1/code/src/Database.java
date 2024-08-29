import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database<T> {
    private String fileName;
    private static final String USER_DATA_FOLDER = "UserData";

    public Database(String fileName) {
        this.fileName = USER_DATA_FOLDER + File.separator + fileName;
        createUserDataFolder();
    }

    private void createUserDataFolder() {
        File directory = new File(USER_DATA_FOLDER);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public void saveToFile(List<T> items) {
        File file = new File(fileName);
        file.getParentFile().mkdirs(); // Ensure the directory exists
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
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
}