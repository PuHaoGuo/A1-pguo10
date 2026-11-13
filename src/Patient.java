public class Patient {
    private String name;
    private String phoneNumber;

    public Patient(String name, String phoneNumber) {
        if(name == null || name.isBlank() || phoneNumber == null || phoneNumber.isBlank()){
            throw new IllegalArgumentException("Patient name and phone number cannot be empty!");
        }
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Patient: " + name + ", Phone: " + phoneNumber;
    }
}