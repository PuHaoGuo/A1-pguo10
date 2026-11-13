import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AssignmentOne {
    public static void main(String[] args) {
        System.out.println("===== Part 3: Health Professionals List =====");
        Set<String> days1 = new HashSet<>(Arrays.asList("Monday", "Wednesday", "Friday"));
        Set<String> days2 = new HashSet<>(Arrays.asList("Tuesday", "Thursday"));
        Set<String> days3 = new HashSet<>(Arrays.asList("Monday", "Tuesday", "Thursday", "Friday"));

        HealthProfessional gp1 = new GeneralPractitioner(101, "Dr. Alice Brown", days1, true, 12);
        HealthProfessional gp2 = new GeneralPractitioner(102, "Dr. Bob Wilson", days2, false, 8);
        HealthProfessional gp3 = new GeneralPractitioner(103, "Dr. Carol Davis", days3, true, 15);

        HealthProfessional spec1 = new Specialist(201, "Dr. David Lee", days1, "Cardiology");
        HealthProfessional nurse1 = new Nurse(301, "Nurse Emma Clark", days2, "Emergency Ward");
        HealthProfessional diet1 = new Dietitian(401, "Ms. Fiona Green", days3, "Diabetes Care");

        List<HealthProfessional> professionals = Arrays.asList(gp1, gp2, gp3, spec1, nurse1, diet1);
        for(HealthProfessional hp : professionals){
            System.out.println(hp);
        }
        System.out.println();

        System.out.println("===== Part 6: Appointment Management Demo =====");
        AppointmentManager manager = new AppointmentManager();

        Patient p1 = new Patient("John Smith", "0412345678");
        Patient p2 = new Patient("Mary Johnson", "0423456789");
        Patient p3 = new Patient("Tom Williams", "0434567890");

        System.out.println(manager.addAppointment(new Appointment(p1, gp1, "09:00")));
        System.out.println(manager.addAppointment(new Appointment(p2, gp1, "09:30")));
        System.out.println(manager.addAppointment(new Appointment(p3, spec1, "10:00")));
        System.out.println(manager.addAppointment(new Appointment(p1, nurse1, "11:00")));

        System.out.println("\n--- Duplicate appointment test ---");
        System.out.println(manager.addAppointment(new Appointment(p2, gp1, "09:00")));

        System.out.println("\n--- All appointments (sorted) ---");
        manager.sortByTime();
        for(Appointment a : manager.getAllAppointments()){
            System.out.println(a);
        }

        System.out.println("\n--- Filter by professional ID 101 ---");
        List<Appointment> byId = manager.filterByProfessionalId(101);
        for(Appointment a : byId){
            System.out.println(a);
        }

        System.out.println("\n--- Filter by patient phone 0412345678 ---");
        List<Appointment> byPhone = manager.filterByPatientPhone("0412345678");
        manager.sortAppointments(byPhone);

        for(Appointment a : byPhone){
            System.out.println(a);
        }

        System.out.println("\n--- Cancel appointment ---");
        System.out.println("\n--- Cancel by patient phone ---");
System.out.println(manager.cancelAppointment("0434567890"));
System.out.println(manager.cancelAppointment("0499999999"));

        System.out.println(manager.cancelAppointment(101, "09:30"));
        System.out.println(manager.cancelAppointment(999, "10:00"));

        System.out.println("\n--- All appointments after cancellation ---");
        for(Appointment a : manager.getAllAppointments()){
            System.out.println(a);
        }
    }
}
