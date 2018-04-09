package seedu.address.model.person;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import seedu.address.model.person.Appointment.Appointment;
import seedu.address.model.person.Appointment.AppointmentList;
import seedu.address.model.person.timetable.Timetable;

//@@author chenxing1992
/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyPerson {

    ObjectProperty<Name> nameProperty();
    ObjectProperty<Phone> phoneProperty();
    ObjectProperty<Email> emailProperty();
    ObjectProperty<Address> addressProperty();
    ObjectProperty<Timetable> timeTableProperty();
    ObjectProperty<AppointmentList> appointmentProperty();

    Name getName();

    Phone getPhone();

    Email getEmail();

    Address getAddress();

    Timetable getTimetable();

    List<Appointment> getAppointments();

    /**
    /**Same state detected will return true.
     */
    default boolean equals(ReadOnlyPerson rp) {
        return rp == this // short circuit if same object
                || (rp != null // this is first to avoid NPE below
                && rp.getName().equals(this.getName()) // state checks here onwards
                && rp.getPhone().equals(this.getPhone())
                && rp.getEmail().equals(this.getEmail())
                && rp.getAddress().equals(this.getAddress())
                && rp.getTimetable().equals((this.getTimetable())));
    }

    //@@author chenxing1992
    /**
     * Show all contact in detail as text
     */
    default String getAsText() {
        final StringBuilder b = new StringBuilder();
        b.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" TimeTable: ")
                .append(getTimetable());

        return b.toString();
    }

}

