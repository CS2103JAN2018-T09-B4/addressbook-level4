package seedu.address.model.person;

import java.util.List;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import seedu.address.model.person.Appointment.Appointment;
import seedu.address.model.person.Appointment.AppointmentList;
import seedu.address.model.person.timetable.Timetable;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;



//@@author chenxing1992
/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyPerson {

    ObjectProperty<Name> nameProperty();
    Name getName();
    ObjectProperty<Phone> phoneProperty();
    Phone getPhone();
    ObjectProperty<Email> emailProperty();
    Email getEmail();
    ObjectProperty<Address> addressProperty();
    Address getAddress();
    ObjectProperty<Timetable> timeTableProperty();
    Timetable getTimetable();
    ObjectProperty<UniqueTagList> tagProperty();
    Set<Tag> getTags();
    ObjectProperty<AppointmentList> appointmentProperty();
    List<Appointment> getAppointments();
    /**Same state detected will return true.
     */
    default boolean isSameStateAs(ReadOnlyPerson rp) {
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
                .append(getTimetable())
                .append(" Tags: ");
        getTags().forEach(b::append);
        return b.toString();
    }

}

