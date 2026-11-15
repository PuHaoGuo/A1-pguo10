import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class HealthProfessional {
    private int id;
    private String name;
    private Set<String> workingDays;

    public HealthProfessional(int id, String name, Set<String> workingDays) {
        if(id <= 0){
            throw new IllegalArgumentException("ID must be positive number!");
        }
        this.id = id;
        this.name = name;
        this.workingDays = new HashSet<>(workingDays);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getWorkingDays() {
        return new HashSet<>(workingDays);
    }

    public abstract String getJobTitle();

    @Override
    public String toString() {
        return "Job: " + getJobTitle() +
                ", ID: " + id +
                ", Name: " + name +
                ", Working days: " + workingDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthProfessional that = (HealthProfessional) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}        
