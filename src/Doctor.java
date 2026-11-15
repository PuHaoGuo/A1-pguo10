import java.time.DayOfWeek;
import java.util.Set;

/**
 * <b>Presentation order 02/11 | Part 2 | pillars: inheritance, abstraction</b>
 *
 * A health professional who is allowed to practise medicine.
 *
 * <p>This level exists because a general practitioner and a specialist share two
 * things that a nurse and a dietitian do not: a number of years of experience and
 * the authority to prescribe medication. Putting {@link #prescribe} and the years
 * field here means it is written once instead of being duplicated in both doctor
 * subclasses, while nurses and dietitians stay outside the hierarchy and never
 * inherit a behaviour they cannot perform.</p>
 */
public abstract class Doctor extends HealthProfessional {

    // PILLAR 2 (inheritance): yearsOfExperience and prescribe() belong to every
    // doctor, so they live here once instead of being copied into both subclasses.
    // PILLAR 3 (abstraction): still abstract - a "doctor with no specialty" is not
    // a thing the clinic can employ.

    private final int yearsOfExperience;

    protected Doctor(int id, String name, Set<DayOfWeek> workingDays, int yearsOfExperience) {
        super(id, name, workingDays);
        if (yearsOfExperience < 0) {
            throw new IllegalArgumentException(
                    "Years of experience cannot be negative, but was " + yearsOfExperience + ".");
        }
        this.yearsOfExperience = yearsOfExperience;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    /**
     * Writes a prescription. The dosage is validated here rather than in each
     * subclass so that no doctor subtype can bypass the rule.
     *
     * @return a confirmation message, or a warning when the details are incomplete
     */
    public String prescribe(String medication, String dosage) {
        if (medication == null || medication.isBlank() || dosage == null || dosage.isBlank()) {
            return "Warning: a prescription needs both a medication and a dosage.";
        }
        return getName() + " (" + getJobTitle() + ") prescribed " + medication.trim()
                + " " + dosage.trim() + ".";
    }

    @Override
    public String toString() {
        return super.toString() + " | Experience: " + yearsOfExperience + " year(s)";
    }
}
