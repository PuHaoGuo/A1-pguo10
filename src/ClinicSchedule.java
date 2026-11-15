import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The fixed daily roster of bookable time slots.
 *
 * <p>The brief asks how to represent the schedule so that it can change later
 * without breaking the rest of the system. Because this is a class with a start
 * time, an end time and a slot length, changing the clinic from half-hour to
 * twenty-minute appointments is a one-line change in one place; the appointment
 * manager only ever asks {@link #isBookable(LocalTime)} and never hard-codes
 * "08:00" itself.</p>
 *
 * <p>The slot list is built once in the constructor and then handed out read-only,
 * so the roster cannot be edited by accident from outside.</p>
 */
public class ClinicSchedule {

    private final LocalTime start;
    private final LocalTime end;
    private final int slotMinutes;
    private final List<LocalTime> slots;

    /**
     * @param start       first bookable appointment time
     * @param end         last bookable appointment time (inclusive)
     * @param slotMinutes length of each appointment, in minutes
     */
    public ClinicSchedule(LocalTime start, LocalTime end, int slotMinutes) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("The schedule needs a start and an end time.");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("The schedule cannot start after it ends.");
        }
        if (slotMinutes <= 0) {
            throw new IllegalArgumentException(
                    "The slot length must be greater than zero, but was " + slotMinutes + ".");
        }
        this.start = start;
        this.end = end;
        this.slotMinutes = slotMinutes;

        List<LocalTime> built = new ArrayList<>();
        for (LocalTime slot = start; !slot.isAfter(end); slot = slot.plusMinutes(slotMinutes)) {
            built.add(slot);
        }
        this.slots = Collections.unmodifiableList(built);
    }

    /** The clinic's standard day: 08:00 to 16:00 in half-hour appointments. */
    public ClinicSchedule() {
        this(LocalTime.of(8, 0), LocalTime.of(16, 0), 30);
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public int getSlotMinutes() {
        return slotMinutes;
    }

    /** The bookable start times, earliest first. Read-only. */
    public List<LocalTime> getSlots() {
        return slots;
    }

    public int getSlotCount() {
        return slots.size();
    }

    /** True when appointments can start at the given time. */
    public boolean isBookable(LocalTime time) {
        return time != null && slots.contains(time);
    }

    @Override
    public String toString() {
        return start + "-" + end + " (" + slotMinutes + "-minute appointments, "
                + getSlotCount() + " slots)";
    }
}
