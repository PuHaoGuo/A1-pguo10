import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds and manages every appointment the clinic has taken.
 *
 * <p>The collection is an {@code ArrayList} of {@code Appointment}. An array-backed
 * list is the right fit because appointments are added at the end, looked up item by
 * item, and printed in order; there are only a few dozen a day, so there is nothing
 * to gain from a hash-based structure, and a list keeps the sorted output and the
 * "same professional, same time" check simple to reason about.</p>
 *
 * <p>The manager is the only class allowed to change the collection. Callers can
 * read a copy through {@link #getAllAppointments()}, so nothing outside this class
 * can add or remove appointments behind its back.</p>
 */
public class AppointmentManager {

    private final List<Appointment> appointments;
    private final ClinicSchedule schedule;

    public AppointmentManager() {
        this(new ClinicSchedule());
    }

    /** Allows the clinic to run a different daily roster without touching this class. */
    public AppointmentManager(ClinicSchedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("The manager needs a clinic schedule.");
        }
        this.schedule = schedule;
        this.appointments = new ArrayList<>();
    }

    public ClinicSchedule getSchedule() {
        return schedule;
    }

    /**
     * Takes a booking after checking that it is both inside the daily roster and not
     * a double booking for that professional.
     *
     * @return a message describing the outcome; a warning when the booking is refused
     */
    public String addAppointment(Appointment appointment) {
        if (appointment == null) {
            return "Warning: there is no appointment to add.";
        }
        if (!schedule.isBookable(appointment.getTime())) {
            return "Warning: " + appointment.getTime()
                    + " is not a bookable slot. The clinic runs " + schedule + ".";
        }
        if (findByProfessionalAndTime(appointment.getProfessional(), appointment.getTime()) != null) {
            return "Warning: " + appointment.getProfessional().getName()
                    + " already has an appointment at " + appointment.getTime() + ".";
        }
        appointments.add(appointment);
        return "Appointment booked: " + appointment.getPatient().getName()
                + " with " + appointment.getProfessional().getName()
                + " at " + appointment.getTime() + ".";
    }

    /** Every appointment for one professional, in no particular order. */
    public List<Appointment> filterByProfessionalId(int professionalId) {
        List<Appointment> matches = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getProfessional().getId() == professionalId) {
                matches.add(appointment);
            }
        }
        return matches;
    }

    /** Every appointment for one patient, found by the phone number they booked with. */
    public List<Appointment> filterByPatientPhone(String phoneNumber) {
        List<Appointment> matches = new ArrayList<>();
        if (phoneNumber == null) {
            return matches;
        }
        String wanted = phoneNumber.trim();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getPhoneNumber().equals(wanted)) {
                matches.add(appointment);
            }
        }
        return matches;
    }

    /** Sorts the stored appointments from earliest to latest. */
    public void sortByTime() {
        Collections.sort(appointments);
    }

    /**
     * Sorts any list of appointments, so a filtered subset can be shown in time order
     * without disturbing the order of the full collection.
     */
    public void sortAppointments(List<Appointment> list) {
        if (list != null) {
            Collections.sort(list);
        }
    }

    /**
     * Cancels the booking for a professional at a given time.
     *
     * @return a message describing the outcome; a warning when there was no such booking
     */
    public String cancelAppointment(int professionalId, LocalTime time) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getProfessional().getId() == professionalId
                    && appointment.getTime().equals(time)) {
                appointments.remove(i);
                return "Appointment cancelled: " + appointment.getProfessional().getName()
                        + " at " + time + " (" + appointment.getPatient().getName() + ").";
            }
        }
        return "Warning: no appointment found for professional ID " + professionalId
                + " at " + time + ".";
    }

    /**
     * Cancels every appointment held by one patient.
     *
     * <p>A patient can have more than one booking, so this deliberately cancels all
     * of them and reports how many were removed, rather than silently leaving some
     * behind.</p>
     */
    public String cancelAppointment(String phoneNumber) {
        List<Appointment> matches = filterByPatientPhone(phoneNumber);
        if (matches.isEmpty()) {
            return "Warning: no appointment found for phone number " + phoneNumber + ".";
        }
        appointments.removeAll(matches);
        String count = matches.size() == 1 ? "1 appointment" : matches.size() + " appointments";
        return "Cancelled " + count + " for phone number " + phoneNumber.trim() + ".";
    }

    /** A copy, so callers cannot add or remove appointments through the returned list. */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Prints a list of appointments under a heading. Kept here so that the
     * demonstration does not repeat the same printing loop for every view.
     */
    public void displayAppointments(String heading, List<Appointment> list) {
        System.out.println(heading);
        if (list == null || list.isEmpty()) {
            System.out.println("  (no appointments to display)");
            System.out.println();
            return;
        }
        for (Appointment appointment : list) {
            System.out.println(appointment);
            System.out.println();
        }
    }

    /** Returns the booking for that professional at that time, or {@code null}. */
    private Appointment findByProfessionalAndTime(HealthProfessional professional, LocalTime time) {
        for (Appointment appointment : appointments) {
            if (appointment.getProfessional().equals(professional)
                    && appointment.getTime().equals(time)) {
                return appointment;
            }
        }
        return null;
    }
}
