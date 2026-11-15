import java.time.DayOfWeek;
import java.util.Set;

/**
 * <b>Presentation order 06/11 | Part 2 | pillars: inheritance, polymorphism</b>
 *
 * A dietitian, who plans what patients eat rather than what they take.
 */
public class Dietitian extends HealthProfessional {

    private static final String DEFAULT_EXPERTISE = "General Nutrition";

    private final String expertise;

    public Dietitian(int id, String name, Set<DayOfWeek> workingDays, String expertise) {
        super(id, name, workingDays);
        if (expertise == null || expertise.isBlank()) {
            throw new IllegalArgumentException("A dietitian must have an area of expertise.");
        }
        this.expertise = expertise.trim();
    }

    /** A dietitian whose speciality was not supplied is treated as a general dietitian. */
    public Dietitian(int id, String name, Set<DayOfWeek> workingDays) {
        this(id, name, workingDays, DEFAULT_EXPERTISE);
    }

    public String getExpertise() {
        return expertise;
    }

    /**
     * Types out a meal plan goal for the patient. Unique to this class, so it is not
     * on the base type.
     */
    public String createMealPlan(String patientName, String goal) {
        if (patientName == null || patientName.isBlank()) {
            return "Warning: a meal plan needs the patient's name.";
        }
        if (goal == null || goal.isBlank()) {
            return "Warning: a meal plan needs a goal to work towards.";
        }
        return getName() + " prepared a " + expertise + " meal plan for "
                + patientName.trim() + " aiming to " + goal.trim() + ".";
    }

    @Override
    public String getJobTitle() {
        return "Dietitian";
    }

    @Override
    public String performPrimaryDuty() {
        return "Assesses diet and builds eating plans within " + expertise + ".";
    }

    @Override
    public String toString() {
        return super.toString() + " | Expertise: " + expertise;
    }
}
