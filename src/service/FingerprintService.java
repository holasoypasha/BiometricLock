package service;

import model.LockSystem;
import model.User;

public class FingerprintService {

    /** Проверка отпечатка
     * @param fingerprint отпечаток пальца
     * @param system
     * @return
     */
    public static boolean checkFingerprint(String fingerprint, LockSystem system) {
        for (User user : system.getUsers()) {
            if (user.getFingerprint().equals(fingerprint)) {
                return true;
            }
        }
        return false;
    }
}