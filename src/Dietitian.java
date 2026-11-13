import java.util.Set;

public class Dietitian extends HealthProfessional {
    private String expertise;

    public Dietitian(int id, String name, Set<String> workingDays, String expertise) {
        super(id, name, workingDays);
        this.expertise = expertise;
    }

    @Override
    public String getJobTitle() {
        return "Dietitian";
    }

    public String getExpertise() {
        return expertise;
    }

    @Override
    public String toString() {
        return super.toString() + ", Expertise: " + expertise;
    }
} 