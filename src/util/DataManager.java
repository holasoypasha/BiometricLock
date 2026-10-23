package util;

import model.LockSystem;
import model.User;

import java.io.*;
import java.util.ArrayList;

public class DataManager {

    private static final String FILE_NAME = "data.txt";

    public static void saveData(LockSystem system) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));
            writer.println(system.getAdmin().getPinCode());
            writer.println(system.getControlCode());
            for (User user : system.getUsers()) {
                writer.println(user.getFingerprint());
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData(LockSystem system) {
        File file = new File(FILE_NAME);
        if (!file.exists()) {saveData(system);
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String accessCode = reader.readLine();
            String controlCode = reader.readLine();

            system.getAdmin().setPinCode(accessCode);
            system.setControlCode(controlCode);

            ArrayList<User> users = system.getUsers();
            users.clear();

            users.add(new User("Admin", accessCode, "finger1"));

            String fingerprint;

            while ((fingerprint = reader.readLine()) != null) {
                if (!fingerprint.equals("finger1")) {
                    users.add(new User("User", "1111", fingerprint));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}