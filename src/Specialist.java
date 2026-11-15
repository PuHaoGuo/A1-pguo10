import java.util.Set;

public class Specialist extends HealthProfessional {
    private String subSpecialty;

    public Specialist(int id, String name, Set<String> workingDays, String subSpecialty) {
        super(id, name, workingDays);
        this.subSpecialty = subSpecialty;
    }

    @Override
    public String getJobTitle() {
        return "Specialist";
    }

    public String getSubSpecialty() {
        return subSpecialty;
    }

    @Override
    public String toString() {
        return super.toString() + ", Sub-specialty: " + subSpecialty;
    }
}                       


