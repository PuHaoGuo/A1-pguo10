/**
 * The person an appointment belongs to.
 *
 * <p>Patient is a class in its own right rather than two String fields buried inside
 * an appointment, because the same patient can hold several appointments. Modelling
 * them separately means the details are written once and the appointment only has to
 * hold a reference.</p>
 */
public class Patient {

    private final String name;
    private final String phoneNumber;

    public Patient(String name, String phoneNumber) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Patient name cannot be empty.");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Patient phone number cannot be empty.");
        }
        String trimmedPhone = phoneNumber.trim();
        // Digits only, so the same number cannot be stored as "0412 345 678" in one
        // place and "0412345678" in another and then fail to match on a lookup.
        if (!trimmedPhone.matches("\\d{8,15}")) {
            throw new IllegalArgumentException(
                    "Patient phone number must be 8 to 15 digits, but was " + phoneNumber + ".");
        }
        this.name = name.trim();
        this.phoneNumber = trimmedPhone;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * A patient is identified by their phone number, which is exactly the key the
     * appointment manager searches on.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }
        Patient that = (Patient) other;
        return phoneNumber.equals(that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return phoneNumber.hashCode();
    }

    @Override
    public String toString() {
        return name + " (" + phoneNumber + ")";
    }
}
