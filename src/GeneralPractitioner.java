import java.time.DayOfWeek;
import java.util.Set;

/**
 * <b>Presentation order 03/11 | Part 2 | pillars: inheritance, polymorphism</b>
 *
 * A general practitioner: the first point of contact for most patients.
 *
 * <p>On top of everything {@link Doctor} already provides, a GP records whether they
 * bulk bill, because that changes what the patient pays at the desk.</p>
 */
public class GeneralPractitioner extends Doctor {

    private final boolean bulkBilling;

    /**
     * Fully specified general practitioner.
     */
    public GeneralPractitioner(int id, String name, Set<DayOfWeek> workingDays,
            boolean bulkBilling, int yearsOfExperience) {
        super(id, name, workingDays, yearsOfExperience);
        this.bulkBilling = bulkBilling;
    }

    /**
     * A GP whose experience was not recorded yet: defaults to zero years, which is
     * the only honest assumption for a newly registered practitioner.
     */
    public GeneralPractitioner(int id, String name, Set<DayOfWeek> workingDays, boolean bulkBilling) {
        this(id, name, workingDays, bulkBilling, 0);
    }

    /** A GP on the standard Monday-to-Friday roster who does not bulk bill. */
    public GeneralPractitioner(int id, String name) {
        this(id, name, defaultWorkingDays(), false, 0);
    }

    public boolean isBulkBilling() {
        return bulkBilling;
    }

    @Override
    public String getJobTitle() {
        return "General Practitioner";
    }

    @Override
    public String performPrimaryDuty() {
        return "Treats everyday illness and injury, and refers patients on when needed.";
    }

    @Override
    public String toString() {
        return super.toString() + " | Bulk billing: " + (bulkBilling ? "yes" : "no");
    }
}
