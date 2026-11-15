import java.time.LocalTime;

/**
 * One booking: a patient, the health professional they are seeing, and the time.
 *
 * <p>The professional field is declared as {@link HealthProfessional}, not as a
 * {@code GeneralPractitioner}, so a single appointment class works with every type
 * created in Parts 1 and 2 without any special casing.</p>
 *
 * <p>Implementing {@link Comparable} lets a whole collection of appointments sort
 * itself from earliest to latest with one call, and because the time is a
 * {@code LocalTime} rather than text the ordering is genuinely chronological.</p>
 */
public class Appointment implements Comparable<Appointment> {

    private final Patient patient;
    private final HealthProfessional professional;
    private final LocalTime time;

    /**
     * @throws IllegalArgumentException if any of the three details is missing, so an
     *                                  incomplete appointment cannot exist
     */
    public Appointment(Patient patient, HealthProfessional professional, LocalTime time) {
        if (patient == null) {
            throw new IllegalArgumentException("An appointment needs a patient.");
        }
        if (professional == null) {
            throw new IllegalArgumentException("An appointment needs a health professional.");
        }
        if (time == null) {
            throw new IllegalArgumentException("An appointment needs a time.");
        }
        this.patient = patient;
        this.professional = professional;
        this.time = time;
    }

    public Patient getPatient() {
        return patient;
    }

    public HealthProfessional getProfessional() {
        return professional;
    }

    public LocalTime getTime() {
        return time;
    }

    /**
     * True when this appointment and the other one would put the same professional
     * in the same slot twice.
     *
     * <p>The professional comparison goes through {@code equals}, which compares
     * identity rather than object references, so a second object holding the same
     * professional is still recognised as a clash.</p>
     */
    public boolean conflictsWith(Appointment other) {
        return other != null
                && professional.equals(other.professional)
                && time.equals(other.time);
    }

    /**
     * Earliest first. Ties are broken by professional ID and then patient phone so
     * that the printed order is stable between runs; the brief allows any order for
     * ties, but a predictable one makes the demonstration easier to follow.
     */
    @Override
    public int compareTo(Appointment other) {
        int byTime = time.compareTo(other.time);
        if (byTime != 0) {
            return byTime;
        }
        int byProfessional = Integer.compare(professional.getId(), other.professional.getId());
        if (byProfessional != 0) {
            return byProfessional;
        }
        return patient.getPhoneNumber().compareTo(other.patient.getPhoneNumber());
    }

    /**
     * Prints the whole appointment, including the full details of the chosen health
     * professional rather than only their name.
     */
    @Override
    public String toString() {
        return "Appointment " + time + System.lineSeparator()
                + "    Patient:      " + patient + System.lineSeparator()
                + "    Professional: " + professional;
    }
}
