package service;

import model.LockSystem;
import model.User;

public class AdminService {

    /** Добавление отпеатка пальца
     * @param fingerprint отпечаток пальца
     * @param system
     */
    public static void addFingerprint(String fingerprint, LockSystem system) {
        system.getUsers().add(
                new User("New User", "1111", fingerprint)
        );
    }

    /** Смена кода доступа
     * @param controlCode код контроля
     * @param newCode новый код
     * @param system
     * @return
     */
    public static boolean changeAccessCode(String controlCode, String newCode, LockSystem system) {
        if (system.getControlCode().equals(controlCode)) {
            system.getAdmin().setPinCode(newCode);
            return true;
        }
        return false;
    }

    /** Смена кода доступа
     * @param oldCode прошлый код доступа
     * @param newCode новый код доступа
     * @param system
     * @return
     */
    public static boolean changeControlCode(String oldCode, String newCode, LockSystem system) {
        if (system.getControlCode().equals(oldCode)) {
            system.setControlCode(newCode);
            return true;
        }
        return false;
    }
}