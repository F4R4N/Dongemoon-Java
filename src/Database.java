import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Database {
    private static String dataFileName = "Data.bin";
    private static ArrayList<User> users = new ArrayList<User>();

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        Database.users = users;
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static void writeProgramDataToFile() {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(dataFileName));
            os.writeObject(users);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("Data file Not found");
        } catch (IOException e) {
            System.out.println("An error occurred while trying to write data to file.");
        }
    }

    public static void readProgramDataFromFile() {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(dataFileName));
            ArrayList<User> users = (ArrayList<User>) is.readObject();
            is.close();
            Database.setUsers(users);
        } catch (IOException e) {
            
        } catch (ClassNotFoundException e) {
            System.out.println("Something went wrong");
        } catch (ClassCastException e) {
            System.out.println("Something went wrong");
        }
    }

    public static void writePeriodDataToFile(String periodData, String fileName) {
        try {
            PrintWriter out = new PrintWriter(fileName);
            out.print(periodData);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
