import java.time.DayOfWeek;
import java.util.Set;

/**
 * A specialist such as a cardiologist or a paediatrician, who sees patients
 * referred by a general practitioner.
 */
public class Specialist extends Doctor {

    private final String subSpecialty;

    public Specialist(int id, String name, Set<DayOfWeek> workingDays,
            String subSpecialty, int yearsOfExperience) {
        super(id, name, workingDays, yearsOfExperience);
        if (subSpecialty == null || subSpecialty.isBlank()) {
            throw new IllegalArgumentException("A specialist must have a sub-specialty.");
        }
        this.subSpecialty = subSpecialty.trim();
    }

    /** A specialist whose experience was not recorded: defaults to zero years. */
    public Specialist(int id, String name, Set<DayOfWeek> workingDays, String subSpecialty) {
        this(id, name, workingDays, subSpecialty, 0);
    }

    /** A specialist on the standard Monday-to-Friday roster. */
    public Specialist(int id, String name, String subSpecialty) {
        this(id, name, defaultWorkingDays(), subSpecialty, 0);
    }

    public String getSubSpecialty() {
        return subSpecialty;
    }

    @Override
    public String getJobTitle() {
        return "Specialist";
    }

    @Override
    public String performPrimaryDuty() {
        return "Diagnoses and treats complex conditions within " + subSpecialty + ".";
    }

    @Override
    public String toString() {
        return super.toString() + " | Sub-specialty: " + subSpecialty;
    }
}
