import java.util.Set;

public class Nurse extends HealthProfessional {
    private String ward;

    public Nurse(int id, String name, Set<String> workingDays, String ward) {
        super(id, name, workingDays);
        this.ward = ward;
    }

    @Override
    public String getJobTitle() {
        return "Nurse";
    }

    public String getWard() {
        return ward;
    }

    @Override
    public String toString() {
        return super.toString() + ", Ward: " + ward;
    }
}