import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppointmentManager {
    private List<Appointment> appointments;
    private final String[] AVAILABLE_SLOTS = {
            "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
            "14:00", "14:30", "15:00", "15:30", "16:00"
    };

    public AppointmentManager() {
        this.appointments = new ArrayList<>();
    }

    public String addAppointment(Appointment appointment) {
        String slot = appointment.getTimeSlot();
        boolean slotValid = false;
        for(String s : AVAILABLE_SLOTS){
            if(s.equals(slot)){
                slotValid = true;
                break;
            }
        }
        if(!slotValid){
            return "Warning: Time slot " + slot + " is not available.";
        }

        for(Appointment a : appointments){
            if(a.getProfessional().getId() == appointment.getProfessional().getId()
                    && a.getTimeSlot().equals(slot)){
                return "Warning: This professional already has an appointment at " + slot + ".";
            }
        }

        appointments.add(appointment);
        return "Appointment added successfully.";
    }

    public List<Appointment> filterByProfessionalId(int professionalId) {
        List<Appointment> result = new ArrayList<>();
        for(Appointment a : appointments){
            if(a.getProfessional().getId() == professionalId){
                result.add(a);
            }
        }
        return result;
    }

    public List<Appointment> filterByPatientPhone(String phoneNumber) {
        List<Appointment> result = new ArrayList<>();
        for(Appointment a : appointments){
            if(a.getPatient().getPhoneNumber().equals(phoneNumber)){
                result.add(a);
            }
        }
        return result;
    }

  public void sortByTime() {
    sortAppointments(appointments);
}

public void sortAppointments(List<Appointment> list) {
    Collections.sort(list);
}


    public String cancelAppointment(int professionalId, String timeSlot) {
        for(int i = 0; i < appointments.size(); i++){
            Appointment a = appointments.get(i);
            if(a.getProfessional().getId() == professionalId && a.getTimeSlot().equals(timeSlot)){
                appointments.remove(i);
                return "Appointment cancelled successfully.";
            }
        }
        return "Warning: No matching appointment found to cancel.";
    }
public String cancelAppointment(String phoneNumber) {
    for (int i = 0; i < appointments.size(); i++) {
        Appointment a = appointments.get(i);
        if (a.getPatient().getPhoneNumber().equals(phoneNumber)) {
            appointments.remove(i);
            return "Appointment cancelled successfully by phone.";
        }
    }
    return "Warning: No appointment found for phone " + phoneNumber + ".";
}

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }
}