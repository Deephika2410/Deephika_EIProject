package models;

public class PassengerInfo {
    private String name;
    private int age;
    private String idNumber;
    private String contact;

    public PassengerInfo(String name, int age, String idNumber, String contact) {
        this.name = name;
        this.age = age;
        this.idNumber = idNumber;
        this.contact = contact;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getIdNumber() { return idNumber; }
    public String getContact() { return contact; }

    @Override
    public String toString() {
        return name + " (Age: " + age + ", ID: " + idNumber + ")";
    }
}
