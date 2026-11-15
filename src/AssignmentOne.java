import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * <b>Presentation order 11/11 | Parts 3 and 6 | pillar: polymorphism</b>
 *
 * PROG2004 Assignment 1 - appointment system for a health service.
 *
 * <p>This class holds the {@code main} method that demonstrates the whole system.
 * The design lives in the other classes; this file is the scripted simulation that
 * stands in for real users and prints what happens.</p>
 *
 * <p>The demonstration runs in the same order as the assessment brief:
 * Parts 1 to 3 look at health professionals, Parts 4 to 6 at appointments.</p>
 */
public class AssignmentOne {

    private static final String RULE = "=".repeat(78);

    public static void main(String[] args) {
        // The roster is created once and reused by the later parts, so every part
        // is talking about the same clinic rather than a new set of objects.
        GeneralPractitioner gpAlice = new GeneralPractitioner(
                101, "Dr Alice Brown", EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY,
                        DayOfWeek.FRIDAY), true, 12);
        GeneralPractitioner gpBen = new GeneralPractitioner(
                102, "Dr Ben Carter", EnumSet.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), false, 8);
        GeneralPractitioner gpCara = new GeneralPractitioner(103, "Dr Cara Nguyen");
        Specialist drLee = new Specialist(201, "Dr David Lee",
                EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY), "Cardiology", 15);
        Specialist drPatel = new Specialist(202, "Dr Priya Patel",
                EnumSet.of(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY), "Paediatrics", 6);
        Nurse nurseEmma = new Nurse(301, "Nurse Emma Clark",
                EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY),
                "Emergency Ward");
        Dietitian msFiona = new Dietitian(401, "Ms Fiona Green",
                EnumSet.of(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "Diabetes Care");

        List<HealthProfessional> professionals = new ArrayList<>();
        professionals.add(gpAlice);
        professionals.add(gpBen);
        professionals.add(gpCara);
        professionals.add(drLee);
        professionals.add(drPatel);
        professionals.add(nurseEmma);
        professionals.add(msFiona);

        partOneModelsAHealthProfessional();
        partTwoSpecialisesHealthProfessionals(gpAlice, drLee, nurseEmma, msFiona);
        partThreeWorksWithHealthProfessionals(professionals);
        partFourModelsAnAppointment(gpAlice, drLee);
        partFiveManagesAppointments(gpAlice, drLee, nurseEmma);
        partSixDemonstratesEndToEnd(gpAlice, gpBen, drLee, nurseEmma, msFiona);
    }

    // ------------------------------------------------------------------
    // Part 1 - modelling a health professional
    // ------------------------------------------------------------------

    private static void partOneModelsAHealthProfessional() {
        heading("PART 1 - Modelling a health professional");

        System.out.println("HealthProfessional is abstract: there is no such thing as a");
        System.out.println("professional with no specialty, so the common state and behaviour");
        System.out.println("is demonstrated through its subclasses.");
        System.out.println();

        System.out.println("1. Fully specified general practitioner:");
        GeneralPractitioner fullySpecified = new GeneralPractitioner(
                110, "Dr Holly Reed", EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), true, 9);
        System.out.println("   " + fullySpecified);
        System.out.println();

        System.out.println("2. Second constructor: caller supplies only id and name, so the");
        System.out.println("   roster defaults to Monday-Friday and experience to zero:");
        GeneralPractitioner defaulted = new GeneralPractitioner(111, "Dr Ivan Petrov");
        System.out.println("   " + defaulted);
        System.out.println();

        System.out.println("3. Controlled access - the roster cannot be edited from outside:");
        Set<DayOfWeek> callerDays = EnumSet.of(DayOfWeek.MONDAY);
        GeneralPractitioner guarded = new GeneralPractitioner(112, "Dr Jane Okoro", callerDays, false, 4);
        callerDays.add(DayOfWeek.SATURDAY);
        System.out.println("   After the caller added Saturday to its own set, the object still reads:");
        System.out.println("   " + guarded);
        try {
            guarded.getWorkingDays().add(DayOfWeek.SUNDAY);
            System.out.println("   PROBLEM: the internal roster was modified.");
        } catch (UnsupportedOperationException exception) {
            System.out.println("   Attempt to modify the returned set was rejected: "
                    + "UnsupportedOperationException");
        }
        System.out.println("   worksOn(FRIDAY) -> " + guarded.worksOn(DayOfWeek.FRIDAY));
        System.out.println();

        System.out.println("4. The object cannot be built in an invalid state:");
        try {
            new GeneralPractitioner(-5, "Dr Nobody");
            System.out.println("   PROBLEM: a negative id was accepted.");
        } catch (IllegalArgumentException exception) {
            System.out.println("   Rejected negative id -> " + exception.getMessage());
        }
        try {
            new GeneralPractitioner(113, "   ");
            System.out.println("   PROBLEM: a blank name was accepted.");
        } catch (IllegalArgumentException exception) {
            System.out.println("   Rejected blank name -> " + exception.getMessage());
        }
        try {
            new GeneralPractitioner(114, "Dr Weekend", EnumSet.noneOf(DayOfWeek.class), true, 2);
            System.out.println("   PROBLEM: an empty roster was accepted.");
        } catch (IllegalArgumentException exception) {
            System.out.println("   Rejected empty roster -> " + exception.getMessage());
        }
        System.out.println();

        System.out.println("5. Equality is based on identity, not on the object reference:");
        GeneralPractitioner sameId = new GeneralPractitioner(112, "A second object for the same ID",
                EnumSet.of(DayOfWeek.TUESDAY), false, 0);
        System.out.println("   guarded.equals(sameId)         -> " + guarded.equals(sameId));
        System.out.println("   guarded.equals(fullySpecified) -> " + guarded.equals(fullySpecified));
    }

    // ------------------------------------------------------------------
    // Part 2 - specialising health professionals
    // ------------------------------------------------------------------

    private static void partTwoSpecialisesHealthProfessionals(GeneralPractitioner gp,
            Specialist specialist, Nurse nurse, Dietitian dietitian) {
        heading("PART 2 - Four specialised types of health professional");

        System.out.println("Each subtype adds its own details on top of the shared state, then");
        System.out.println("supplies the behaviour that only it can perform.");
        System.out.println();

        System.out.println("General practitioner:");
        System.out.println("   " + gp);
        System.out.println("   Duty: " + gp.performPrimaryDuty());
        System.out.println("   " + gp.prescribe("Amoxicillin", "500 mg three times daily"));
        System.out.println();

        System.out.println("Specialist:");
        System.out.println("   " + specialist);
        System.out.println("   Duty: " + specialist.performPrimaryDuty());
        System.out.println("   " + specialist.prescribe("Atorvastatin", "20 mg nightly"));
        System.out.println();

        System.out.println("Nurse:");
        System.out.println("   " + nurse);
        System.out.println("   Duty: " + nurse.performPrimaryDuty());
        System.out.println("   " + nurse.takeBloodSample("John Smith"));
        System.out.println("   A nurse has no prescribe method, so the rule that only doctors");
        System.out.println("   write prescriptions cannot be broken by a nurse object.");
        System.out.println();

        System.out.println("Dietitian:");
        System.out.println("   " + dietitian);
        System.out.println("   Duty: " + dietitian.performPrimaryDuty());
        System.out.println("   " + dietitian.createMealPlan("Mary Johnson", "lower blood sugar"));
        System.out.println();

        System.out.println("A prescription with incomplete details is refused:");
        System.out.println("   " + gp.prescribe("Amoxicillin", ""));
    }

    // ------------------------------------------------------------------
    // Part 3 - working with health professional objects
    // ------------------------------------------------------------------

    private static void partThreeWorksWithHealthProfessionals(List<HealthProfessional> professionals) {
        heading("PART 3 - Working with a mix of health professionals");

        System.out.println("All seven professionals are stored in one List<HealthProfessional>.");
        System.out.println("One loop prints them, and each object answers performPrimaryDuty()");
        System.out.println("in its own way through polymorphism. Adding a fifth type later");
        System.out.println("would not require a single change in this method.");
        System.out.println();

        int position = 1;
        // PILLAR 4 (polymorphism): one list, one loop, no instanceof and no cast.
        // The object itself decides which performPrimaryDuty() runs.
        for (HealthProfessional professional : professionals) {
            System.out.println(position + ". " + professional);
            System.out.println("   Duty: " + professional.performPrimaryDuty());
            position++;
        }
        System.out.println();
        System.out.println("Total professionals on the roster: " + professionals.size());
    }

    // ------------------------------------------------------------------
    // Part 4 - modelling an appointment
    // ------------------------------------------------------------------

    private static void partFourModelsAnAppointment(HealthProfessional gp, HealthProfessional specialist) {
        heading("PART 4 - Modelling an appointment");

        Patient john = new Patient("John Smith", "0412345678");
        Patient mary = new Patient("Mary Johnson", "0423456789");

        Appointment oneBooking = new Appointment(john, gp, LocalTime.parse("09:30"));
        System.out.println("One appointment, showing the patient and the full professional details:");
        System.out.println(oneBooking);
        System.out.println();

        System.out.println("The same appointment class works with any professional type:");
        System.out.println(new Appointment(mary, specialist, LocalTime.parse("10:00")));
        System.out.println();

        System.out.println("Appointments order themselves earliest to latest because the time is");
        System.out.println("a LocalTime and the class implements Comparable:");
        List<Appointment> bookedInAnyOrder = new ArrayList<>();
        bookedInAnyOrder.add(new Appointment(mary, specialist, LocalTime.parse("14:30")));
        bookedInAnyOrder.add(new Appointment(john, gp, LocalTime.parse("08:30")));
        bookedInAnyOrder.add(new Appointment(mary, gp, LocalTime.parse("11:00")));
        bookedInAnyOrder.add(new Appointment(john, specialist, LocalTime.parse("08:30")));
        Collections.sort(bookedInAnyOrder);
        for (Appointment appointment : bookedInAnyOrder) {
            System.out.println("   " + appointment.getTime() + "  "
                    + appointment.getProfessional().getName()
                    + " for " + appointment.getPatient().getName());
        }
        System.out.println("   (08:30 appears twice, for two different professionals)");
        System.out.println();

        System.out.println("An incomplete appointment cannot be created:");
        try {
            new Appointment(null, gp, LocalTime.parse("09:00"));
            System.out.println("   PROBLEM: an appointment with no patient was accepted.");
        } catch (IllegalArgumentException exception) {
            System.out.println("   Rejected missing patient -> " + exception.getMessage());
        }
        try {
            new Appointment(john, null, LocalTime.parse("09:00"));
            System.out.println("   PROBLEM: an appointment with no professional was accepted.");
        } catch (IllegalArgumentException exception) {
            System.out.println("   Rejected missing professional -> " + exception.getMessage());
        }
        try {
            new Appointment(john, gp, null);
            System.out.println("   PROBLEM: an appointment with no time was accepted.");
        } catch (IllegalArgumentException exception) {
            System.out.println("   Rejected missing time -> " + exception.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Part 5 - managing the collection of appointments
    // ------------------------------------------------------------------

    private static void partFiveManagesAppointments(GeneralPractitioner gp,
            Specialist specialist, Nurse nurse) {
        heading("PART 5 - Managing the collection of appointments");

        AppointmentManager manager = new AppointmentManager();
        System.out.println("Clinic schedule: " + manager.getSchedule());
        System.out.println("The manager asks the schedule what is bookable instead of");
        System.out.println("hard-coding the times, so a different roster just works.");
        System.out.println();

        Patient john = new Patient("John Smith", "0412345678");
        Patient mary = new Patient("Mary Johnson", "0423456789");
        Patient tom = new Patient("Tom Williams", "0434567890");

        System.out.println("Adding appointments:");
        System.out.println("   " + manager.addAppointment(new Appointment(john, gp, LocalTime.parse("09:00"))));
        System.out.println("   " + manager.addAppointment(new Appointment(mary, gp, LocalTime.parse("09:30"))));
        System.out.println("   " + manager.addAppointment(new Appointment(tom, specialist, LocalTime.parse("10:00"))));
        System.out.println("   " + manager.addAppointment(new Appointment(john, nurse, LocalTime.parse("11:00"))));
        System.out.println();

        System.out.println("Refusing a double booking for the same professional and time:");
        System.out.println("   " + manager.addAppointment(new Appointment(mary, gp, LocalTime.parse("09:00"))));
        System.out.println();

        System.out.println("Refusing times outside the daily schedule:");
        System.out.println("   " + manager.addAppointment(new Appointment(tom, gp, LocalTime.parse("07:30"))));
        System.out.println("   " + manager.addAppointment(new Appointment(tom, gp, LocalTime.parse("16:30"))));
        System.out.println();

        System.out.println("Sending a null appointment is refused rather than crashing:");
        System.out.println("   " + manager.addAppointment(null));
        System.out.println();

        manager.sortByTime();
        manager.displayAppointments("All appointments, sorted by time:", manager.getAllAppointments());

        manager.displayAppointments("Filtered by professional ID 101:",
                manager.filterByProfessionalId(101));

        List<Appointment> johnsBookings = manager.filterByPatientPhone("0412345678");
        manager.sortAppointments(johnsBookings);
        manager.displayAppointments("Filtered by patient phone 0412345678, sorted by time:",
                johnsBookings);

        manager.displayAppointments("Filtered by professional ID 999 (nobody):",
                manager.filterByProfessionalId(999));

        System.out.println("Cancelling by professional ID and time:");
        System.out.println("   " + manager.cancelAppointment(101, LocalTime.parse("09:30")));
        System.out.println("   " + manager.cancelAppointment(999, LocalTime.parse("10:00")));
        System.out.println();

        System.out.println("Cancelling by patient phone number:");
        System.out.println("   " + manager.cancelAppointment("0434567890"));
        System.out.println("   " + manager.cancelAppointment("0499999999"));
        System.out.println();

        manager.displayAppointments("Remaining appointments:", manager.getAllAppointments());
    }

    // ------------------------------------------------------------------
    // Part 6 - working with appointments end to end
    // ------------------------------------------------------------------

    private static void partSixDemonstratesEndToEnd(GeneralPractitioner gpAlice,
            GeneralPractitioner gpBen, Specialist specialist, Nurse nurse, Dietitian dietitian) {
        heading("PART 6 - Appointment system end to end");

        AppointmentManager manager = new AppointmentManager();
        Patient john = new Patient("John Smith", "0412345678");
        Patient mary = new Patient("Mary Johnson", "0423456789");
        Patient tom = new Patient("Tom Williams", "0434567890");
        Patient ana = new Patient("Ana Souza", "0455667788");

        System.out.println("Step 1 - book five appointments across four professional types.");
        System.out.println("   " + manager.addAppointment(new Appointment(mary, specialist, LocalTime.parse("14:00"))));
        System.out.println("   " + manager.addAppointment(new Appointment(john, gpAlice, LocalTime.parse("09:00"))));
        System.out.println("   " + manager.addAppointment(new Appointment(tom, nurse, LocalTime.parse("11:30"))));
        System.out.println("   " + manager.addAppointment(new Appointment(ana, dietitian, LocalTime.parse("13:00"))));
        System.out.println("   " + manager.addAppointment(new Appointment(john, gpBen, LocalTime.parse("08:30"))));
        System.out.println();

        System.out.println("Step 2 - the error cases: a double booking and a time outside the roster.");
        System.out.println("   " + manager.addAppointment(new Appointment(ana, gpAlice, LocalTime.parse("09:00"))));
        System.out.println("   " + manager.addAppointment(new Appointment(ana, gpAlice, LocalTime.parse("09:15"))));
        System.out.println();

        manager.sortByTime();
        manager.displayAppointments("Step 3 - every appointment, earliest first:",
                manager.getAllAppointments());

        manager.displayAppointments("Step 4 - filtered by patient phone 0412345678 (John has two):",
                manager.filterByPatientPhone("0412345678"));
        manager.displayAppointments("Step 5 - filtered by professional ID 201:",
                manager.filterByProfessionalId(201));

        System.out.println("The manager spots a clash through Appointment.conflictsWith():");
        Appointment slotTaken = new Appointment(ana, gpAlice, LocalTime.parse("09:00"));
        Appointment sameSlot = new Appointment(tom, gpAlice, LocalTime.parse("09:00"));
        Appointment nextSlot = new Appointment(tom, gpAlice, LocalTime.parse("09:30"));
        System.out.println("   same professional, same time  -> " + slotTaken.conflictsWith(sameSlot));
        System.out.println("   same professional, other time -> " + slotTaken.conflictsWith(nextSlot));
        System.out.println();

        System.out.println("Step 6 - cancel one booking by patient phone, then one by ID and time:");
        System.out.println("   " + manager.cancelAppointment("0434567890"));
        System.out.println("   " + manager.cancelAppointment(201, LocalTime.parse("14:00")));
        System.out.println();

        manager.displayAppointments("Step 7 - the collection after the cancellations:",
                manager.getAllAppointments());

        System.out.println("Step 8 - cancelling something that is not there is refused:");
        System.out.println("   " + manager.cancelAppointment(201, LocalTime.parse("14:00")));
        System.out.println("   " + manager.cancelAppointment("0411111111"));
        System.out.println();

        System.out.println("Appointments still held: " + manager.getAppointmentCount());
        System.out.println();
        System.out.println(RULE);
        System.out.println("End of demonstration.");
        System.out.println(RULE);
    }

    // ------------------------------------------------------------------
    // Output helpers - kept in one place so headings stay consistent
    // ------------------------------------------------------------------

    private static void heading(String title) {
        System.out.println();
        System.out.println(RULE);
        System.out.println(title);
        System.out.println(RULE);
    }
}
