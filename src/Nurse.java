import java.time.DayOfWeek;
import java.util.Set;

/**
 * <b>Presentation order 05/11 | Part 2 | pillars: inheritance, polymorphism</b>
 *
 * A nurse: the blood samples and day-to-day patient care end of the clinic.
 *
 * <p>A nurse deliberately does <em>not</em> extend {@link Doctor}. If the class sat
 * under {@code Doctor} it would inherit {@code prescribe}, and the clinic's rule that
 * only doctors write prescriptions could be broken by accident.</p>
 */
public class Nurse extends HealthProfessional {

    private static final String DEFAULT_WARD = "General Ward";

    private final String ward;

    public Nurse(int id, String name, Set<DayOfWeek> workingDays, String ward) {
        super(id, name, workingDays);
        if (ward == null || ward.isBlank()) {
            throw new IllegalArgumentException("A nurse must be assigned to a ward.");
        }
        this.ward = ward.trim();
    }

    /** A nurse whose ward was not supplied is placed on the general ward. */
    public Nurse(int id, String name, Set<DayOfWeek> workingDays) {
        this(id, name, workingDays, DEFAULT_WARD);
    }

    public String getWard() {
        return ward;
    }

    /**
     * A behaviour only a nurse offers. It is deliberately not declared on the base
     * class, because nothing else in the clinic can perform it.
     *
     * @return a confirmation message for the sample that was taken
     */
    public String takeBloodSample(String patientName) {
        if (patientName == null || patientName.isBlank()) {
            return "Warning: a blood sample needs the patient's name.";
        }
        return getName() + " took a blood sample from " + patientName.trim()
                + " for the laboratory.";
    }

    @Override
    public String getJobTitle() {
        return "Nurse";
    }

    @Override
    public String performPrimaryDuty() {
        return "Provides patient care, takes observations and samples, and prepares patients.";
    }

    @Override
    public String toString() {
        return super.toString() + " | Ward: " + ward;
    }
}
