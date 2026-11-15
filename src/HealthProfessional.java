import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * <b>Presentation order 01/11 | Part 1 | pillars: encapsulation, abstraction</b>
 *
 * The common parent of every type of health professional the clinic employs.
 *
 * <p>Everything that is true of <em>any</em> health professional lives here: the
 * identifier, the name and the fixed set of days the professional works. Anything
 * that differs between a general practitioner, a specialist, a nurse and a
 * dietitian is pushed down into the subclasses, which is what keeps the shared
 * information in exactly one place (inheritance instead of copy-paste).</p>
 *
 * <p>The class is {@code abstract} on purpose: there is no such thing as a health
 * professional "in general" in this clinic, so the type cannot be instantiated
 * directly and every concrete subclass is forced to supply the two abstract
 * behaviours below.</p>
 *
 * <p>Objects are immutable. Every field is {@code final}, the constructor is the
 * only place that can write to them, and all values are validated there, so an
 * object can never be created in an invalid state. Access is controlled through
 * getters only, and the working-day set is handed out as a read-only view.</p>
 */
public abstract class HealthProfessional {

    // PILLAR 3 (abstraction): the class is abstract, so "new HealthProfessional(...)"
    // is a compile error. This is intentional - see getJobTitle / performPrimaryDuty.

    /** Used by the two-argument constructor when the caller does not supply a working week. */
    private static final Set<DayOfWeek> DEFAULT_WORKING_DAYS =
            EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);

    // PILLAR 1 (encapsulation): every field is private and final. They are written
    // once, in the constructor, and only after validation, so the object can never
    // be observed in an invalid state.
    private final int id;
    private final String name;
    private final Set<DayOfWeek> workingDays;

    /**
     * Creates a health professional with an explicit working week.
     *
     * @param id          identifier made of digits only, must be positive
     * @param name        the professional's full name, must not be blank
     * @param workingDays the days the clinic can book this professional, must not be empty
     * @throws IllegalArgumentException if any argument would leave the object in an invalid state
     */
    public HealthProfessional(int id, String name, Set<DayOfWeek> workingDays) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Health professional ID must be a positive number, but was " + id + ".");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Health professional name cannot be empty.");
        }
        if (workingDays == null || workingDays.isEmpty()) {
            throw new IllegalArgumentException(
                    "A health professional must work on at least one day of the week.");
        }
        this.id = id;
        this.name = name.trim();
        // Defensive copy: the caller keeps a reference to the set it passed in, and
        // without this it could change our working days afterwards.
        this.workingDays = EnumSet.copyOf(workingDays);
    }

    /**
     * Convenience constructor: a professional who was not given a roster works the
     * standard Monday-to-Friday week. This is the "sensible default" the brief asks
     * about, and it means the common case needs the fewest arguments.
     */
    public HealthProfessional(int id, String name) {
        this(id, name, defaultWorkingDays());
    }

    /** Returns a fresh copy of the standard Monday-to-Friday week for subclass constructors. */
    protected static Set<DayOfWeek> defaultWorkingDays() {
        return EnumSet.copyOf(DEFAULT_WORKING_DAYS);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns a read-only view of the roster. Callers can inspect it but cannot
     * change our internal state through it.
     */
    public Set<DayOfWeek> getWorkingDays() {
        return Collections.unmodifiableSet(workingDays);
    }

    /** True when the clinic can book this professional on the given day. */
    public boolean worksOn(DayOfWeek day) {
        return workingDays.contains(day);
    }

    /**
     * The kind of health professional this object is. Declared abstract so that the
     * base class never has to know the list of subclasses, and so that every new
     * type is forced to describe itself.
     */
    public abstract String getJobTitle();

    /**
     * The one thing this type of professional spends their day doing. Called through
     * a {@code HealthProfessional} reference it produces a different answer per
     * subclass, which is the polymorphism demonstrated in Part 3.
     */
    public abstract String performPrimaryDuty();

    /** Renders the roster as "Monday, Wednesday, Friday" rather than [MONDAY, WEDNESDAY]. */
    protected String formatWorkingDays() {
        StringBuilder text = new StringBuilder();
        for (DayOfWeek day : workingDays) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(day.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }
        return text.toString();
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Name: " + name
                + " | Role: " + getJobTitle()
                + " | Works: " + formatWorkingDays();
    }

    /**
     * Two professionals are the same professional when they are the same concrete
     * type and carry the same identifier. Identity, not the object reference, is
     * what the appointment manager needs when it checks for a double booking.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        HealthProfessional that = (HealthProfessional) other;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }
}
