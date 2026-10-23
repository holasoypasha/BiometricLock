package model;

import java.util.ArrayList;

public class LockSystem {

    private boolean locked = true;
    private String controlCode = "9999";
    private ArrayList<User> users = new ArrayList<>();

    public LockSystem() {
        users.add(new User("Admin", "1234", "finger1"));
    }

    public User getAdmin() {
        return users.get(0);
    }

    public boolean isLocked() {
        return locked;
    }

    public void unlock() {
        locked = false;
    }

    public void lock() {
        locked = true;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public String getControlCode() {
        return controlCode;
    }

    public void setControlCode(String controlCode) {
        this.controlCode = controlCode;
    }

}