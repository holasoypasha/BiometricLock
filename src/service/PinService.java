package service;

import model.LockSystem;
import model.User;

public class PinService {
    public static boolean checkPin(String pin, LockSystem system) {
        for (User user : system.getUsers()) {
            if (user.getPinCode().equals(pin)) {
                return true;
            }
        }
        return false;
    }
}