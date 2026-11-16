# PROG2004 Assessment 1 - Appointment System for a Health Service

Student: PuHao Guo - 25258677

An object-oriented backbone for a clinic appointment system, written in Java.
There is no user interface: `AssignmentOne.main` is a scripted simulation that
creates objects, exercises their behaviour and prints the results.

---

## How to run

```
cd src
javac -d ../bin *.java
java -cp ../bin AssignmentOne
```

Or in VS Code, run the `AssignmentOne` launch configuration.

---

## Suggested reading order

The classes build on each other, so this is the easiest order to read them in:

| # | File | What it demonstrates |
|---|---|---|
| 01 | `HealthProfessional.java` | encapsulation: fields, validation, read-only access |
| 02 | `Doctor.java` | inheritance: the shared doctor layer |
| 03 | `GeneralPractitioner.java` | inheritance: chained constructors |
| 04 | `Specialist.java` | inheritance: `super(...)` |
| 05 | `Nurse.java` | inheritance: deliberately outside `Doctor` |
| 06 | `Dietitian.java` | inheritance: own behaviour |
| 07 | `Patient.java` | encapsulation: phone validation, identity |
| 08 | `Appointment.java` | polymorphism: field typed as the parent |
| 09 | `ClinicSchedule.java` | encapsulation: configurable roster |
| 10 | `AppointmentManager.java` | double-booking check via `equals` |
| 11 | `AssignmentOne.java` | polymorphism: one loop, no cast |

---

## Class map

```
                        HealthProfessional  (abstract)
                        id, name, workingDays
                        + getJobTitle()        abstract
                        + performPrimaryDuty() abstract
                        + worksOn(DayOfWeek)
                        + equals/hashCode  (by class + id)
                              /                \
                             /                  \
                  Doctor (abstract)              Nurse          Dietitian
                  yearsOfExperience              ward           expertise
                  + prescribe(med, dose)         + takeBloodSample()
                        /        \
        GeneralPractitioner     Specialist
        bulkBilling             subSpecialty

  Patient                     Appointment                      ClinicSchedule
  name, phone                 patient, professional, time      start, end, slotMinutes
  + equals (by phone)         + compareTo (by time)            + isBookable(LocalTime)
                              + conflictsWith(Appointment)

                        AppointmentManager
                        List<Appointment> + ClinicSchedule
                        add / filter / sort / cancel / display
```

---

## Design decisions, part by part

### Part 1 - HealthProfessional

* **`abstract`, not concrete.** There is no such thing as a health professional
  with no specialty, so the type cannot be instantiated. Every subclass is then
  forced to answer `getJobTitle()` and `performPrimaryDuty()`.
* **Immutable.** All fields are `final` and are only written in the constructor,
  after validation. An object therefore cannot be created - or later mutated -
  into an invalid state.
* **Controlled access.** Getters only. The working-day set is a `Set<DayOfWeek>`
  held internally and handed out through `Collections.unmodifiableSet`, and the
  constructor takes a defensive copy, so a caller cannot change the roster by
  keeping a reference to the set it passed in.
* **Validation.** `id` must be positive, `name` must not be blank, and the roster
  must contain at least one day; each failure throws `IllegalArgumentException`
  with a message that says what was wrong.
* **Two constructors.** `HealthProfessional(id, name, workingDays)` for the
  general case and `HealthProfessional(id, name)` which defaults the roster to
  Monday-Friday - the sensible default the brief asks about.
* **`equals`/`hashCode`.** Two professionals are the same professional when they
  are the same concrete type and hold the same ID. This is what the double-booking
  check relies on, and it is why a clash is still detected when a second object
  holding the same professional is used.

`java.time.DayOfWeek` is used instead of raw strings so that an invalid day such
as `"Wensday"` cannot be stored at all.

### Part 2 - Four specialisations

`GeneralPractitioner`, `Specialist`, `Nurse`, `Dietitian` - one GP plus three other
types, as required. Each adds its own fields and its own behaviour:

| Type | Extra state | Behaviour only it has |
|---|---|---|
| GeneralPractitioner | `bulkBilling` | inherits `prescribe` |
| Specialist | `subSpecialty` | inherits `prescribe` |
| Nurse | `ward` | `takeBloodSample(patient)` |
| Dietitian | `expertise` | `createMealPlan(patient, goal)` |

* **The extra `Doctor` level.** A GP and a specialist both have years of
  experience and both may prescribe; a nurse and a dietitian may do neither. Putting
  `yearsOfExperience` and `prescribe` in an intermediate abstract `Doctor` class
  writes that shared behaviour once instead of duplicating it, and keeps
  `prescribe` off the types that must not have it. `Nurse` deliberately does **not**
  extend `Doctor`, so the clinic's "only doctors prescribe" rule cannot be broken
  by accident.
* **Constructor chaining.** Each subclass constructor calls `super(...)` first, so
  the shared state is validated before the type-specific state is added. Each also
  has shorter overloads that supply defaults (for example a nurse with no ward
  supplied goes to the General Ward).
* **`toString` chaining.** `super.toString()` builds the shared part and each
  subclass appends its own, so the shared details are formatted in exactly one
  place.

### Part 3 - Working with the objects

One `List<HealthProfessional>` holds all seven professionals and a single loop
prints them and calls `performPrimaryDuty()`. There is no `instanceof` and no
cast: each object answers for itself. Adding a fifth type would require no change
to this loop at all.

### Part 4 - Appointment

* The professional field is typed as `HealthProfessional`, so one appointment class
  works with every subtype.
* All three arguments are validated, so an incomplete appointment cannot exist.
* The time is a **`LocalTime`**, not a `String`. Ordering is chronological rather
  than alphabetical, and `compareTo` breaks ties by professional ID and then
  patient phone so the printed order is stable.
* `conflictsWith(other)` puts the "same professional, same time" rule in the
  appointment itself, where the data lives.
* `toString` prints the whole appointment including the **full details of the
  chosen professional**, not only their name.

### Part 5 - AppointmentManager

* **`ArrayList<Appointment>`** for the collection: appointments are appended,
  scanned item by item, and displayed in order, and there are only a few dozen a
  day, so a list is simpler and more predictable than a hash-based structure.
* **`ClinicSchedule` is a class, not a hard-coded array.** Start time, end time and
  slot length are stored and the slot list is generated once. Changing the clinic
  to 20-minute appointments is a single constructor argument; `AppointmentManager`
  only ever asks `schedule.isBookable(time)`.
* `addAppointment` refuses out-of-schedule times and double bookings, and returns a
  readable message either way.
* Filtering by professional ID and by patient phone number; sorting either the full
  collection or a filtered subset.
* Cancelling by `(professionalId, time)` or by phone number. Phone cancellation
  removes **all** of that patient's bookings and reports how many, because a patient
  can legitimately hold more than one.
* `getAllAppointments()` returns a copy, so nothing outside the manager can change
  the collection.
* `displayAppointments(heading, list)` keeps the printing loop in one place instead
  of repeating it for every view.

### Part 6 - End-to-end demonstration

`AssignmentOne.main` runs Parts 1-6 in order. The Part 6 scenario books five
appointments across four professional types, shows a double booking and an
out-of-schedule time being refused, displays the collection in time order, filters
it two different ways, cancels by phone and by ID + time, then displays the
collection again so the change is visible. It ends with two cancellations that
cannot be performed.

Both normal and error cases are demonstrated for every operation.

---

## The four pillars, and where they are in this code

Use this while recording the video.

**1. Encapsulation** - `HealthProfessional`, `Patient`, `Appointment`,
`ClinicSchedule`.
Private `final` fields, validation in the constructor, getters only, defensive
copies, and `getWorkingDays()`/`getAllAppointments()` returning read-only or copied
collections. Nothing outside a class can put it into an invalid state. Chosen
because a health professional's ID and roster must never be reassigned by accident
in the middle of a booking.

**2. Inheritance** - `HealthProfessional` -> `Doctor` -> `GeneralPractitioner` /
`Specialist`, and `HealthProfessional` -> `Nurse` / `Dietitian`.
Shared state and behaviour are written once in the parent; subclasses add only what
differs and call `super(...)` / `super.toString()`. Chosen so that adding a fifth
type cannot introduce a second, divergent copy of the ID/name/roster rules.

**3. Abstraction** - `HealthProfessional` and `Doctor` are `abstract`;
`getJobTitle()` and `performPrimaryDuty()` are abstract methods.
The rest of the system deals with `HealthProfessional` and never needs to know the
concrete types. Chosen because "a health professional with no specialty" is not a
thing the clinic can employ, and because it forces every new subtype to describe
itself.

**4. Polymorphism** - `performPrimaryDuty()` and `toString()` overridden in each
subclass, called through a `HealthProfessional` reference in the Part 3 loop; the
`Appointment` field typed as `HealthProfessional` accepts any subtype.
Chosen so the printing loop and the appointment class never need a per-type branch.

---

## Comments

Inline comments explain *why* a decision was made (defensive copy, identity-based
equality, schedule lookup) rather than restating what the code already says.
Repetitive `javadoc` on trivial getters has been deliberately left out.

---

## Submission checklist

- [ ] Java project with all source files uploaded to MySCU
- [ ] GitHub repository link in the submission comments
      (private repo: invite the tutor and unit assessor as collaborators)
- [ ] WakaTime report attached as `A1-<username>-WakaTime.[png/jpg/pdf]`
- [ ] Video link (<= 5 minutes) in the submission comments
- [ ] GenAI declaration in the comments - see below

### GenAI declaration

Replace this with the statement that actually applies, and delete the other one.

> I acknowledge that I have used Generative AI tools to complete this assessment in
> accordance with the conditions outlined in the Assessment Brief. I used
> **[tool name]** to **[describe what you used it for]**.

> I acknowledge that I have not knowingly used GenAI to complete this assessment.

The unit assessor may ask you to explain how the tools were used, so be ready to
walk through every class in the video.
