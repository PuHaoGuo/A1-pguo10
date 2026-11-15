public class Appointment implements Comparable<Appointment> {
    private Patient patient;
    private HealthProfessional professional;
    private String timeSlot;

    public Appointment(Patient patient, HealthProfessional professional, String timeSlot) {
        if(patient == null || professional == null || timeSlot == null || timeSlot.isBlank()){
            throw new IllegalArgumentException("Appointment details cannot be empty!");
        }
        this.patient = patient;
        this.professional = professional;
        this.timeSlot = timeSlot;
    }

    public Patient getPatient() {
        return patient;
    }

    public HealthProfessional getProfessional() {
        return professional;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    @Override
    public int compareTo(Appointment other) {
        return this.timeSlot.compareTo(other.timeSlot);
    }

    @Override
    public String toString() {
        return "Appointment [" + timeSlot + "] - " + professional.getName() + " (" + professional.getJobTitle() + ") - " + patient.getName() + " (" + patient.getPhoneNumber() + ")";
    }
}    