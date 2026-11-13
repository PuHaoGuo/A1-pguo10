import java.util.Set;

public class GeneralPractitioner extends HealthProfessional {
    private boolean bulkBilling;
    private int yearsOfExperience;

    public GeneralPractitioner(int id, String name, Set<String> workingDays, boolean bulkBilling, int yearsOfExperience) {
        super(id, name, workingDays);
        this.bulkBilling = bulkBilling;
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public String getJobTitle() {
        return "General Practitioner";
    }

    public boolean isBulkBilling() {
        return bulkBilling;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Bulk billing: " + bulkBilling +
                ", Years of experience: " + yearsOfExperience;
    }
}     