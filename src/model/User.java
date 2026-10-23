package model;

public class User {

    private String name;
    private String pinCode;
    private String fingerprint;

    public User(String name, String pinCode, String fingerprint) {
        this.name = name;
        this.pinCode = pinCode;
        this.fingerprint = fingerprint;
    }

    public String getName() {
        return name;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}