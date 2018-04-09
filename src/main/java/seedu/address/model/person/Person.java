package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import seedu.address.model.person.Appointment.Appointment;
import seedu.address.model.person.Appointment.AppointmentList;
import seedu.address.model.person.timetable.Timetable;

//@@author chenxing1992
/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person implements ReadOnlyPerson {

    public static final int PARTNER_INDEX = 0;

    private ObjectProperty<Name> name;
    private ObjectProperty<Phone> phone;
    private ObjectProperty<Email> email;
    private ObjectProperty<Address> address;
    private ObjectProperty<Timetable> timetable;
    private ObjectProperty<AppointmentList> appointments;

    //@@author chenxing1992
    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address,
                  Timetable timetable, List<Appointment> appointments) {

        requireAllNonNull(name, phone, email, address, timetable, appointments);

        this.name = new SimpleObjectProperty<>(name);
        this.phone = new SimpleObjectProperty<>(phone);
        this.email = new SimpleObjectProperty<>(email);
        this.address = new SimpleObjectProperty<>(address);
        this.timetable = new SimpleObjectProperty<>(timetable);
        this.appointments = new SimpleObjectProperty<>(new AppointmentList(appointments));

    }

    //@@author chenxing1992
    /**
     * Creates a copy of the given ReadOnlyPerson.
     */
    public Person(ReadOnlyPerson source) {
        this(source.getName(), source.getPhone(), source.getEmail(), source.getAddress(),
                source.getTimetable(), source.getAppointments());
    }

    @Override
    public ObjectProperty<Name> nameProperty() {
        return name;
    }
    @Override
    public ObjectProperty<Phone> phoneProperty() {
        return phone;
    }
    @Override
    public ObjectProperty<Email> emailProperty() {
        return email;
    }
    @Override
    public ObjectProperty<Address> addressProperty() {
        return address;
    }
    @Override
    public ObjectProperty<Timetable> timeTableProperty() {
        return timetable;
    }
    @Override
    public ObjectProperty<AppointmentList> appointmentProperty() {
        return appointments;
    }

    @Override
    public Name getName() {
        return name.get();
    }

    @Override
    public Phone getPhone() {
        return phone.get();
    }

    @Override
    public Email getEmail() {
        return email.get();
    }

    @Override
    public Address getAddress() {
        return address.get();
    }

    @Override
    public Timetable getTimetable() {
        return timetable.get();
    }

    @Override
    public List<Appointment> getAppointments() {
        return appointments.get().toList();
    }

    public void setAppointment(List<Appointment> appointments) {
        this.appointments.set(new AppointmentList(appointments));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyPerson // instanceof handles nulls
                && this.equals((ReadOnlyPerson) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, timetable);
    }

    @Override
    public String toString() {
        return getName().toString();
    }
}

