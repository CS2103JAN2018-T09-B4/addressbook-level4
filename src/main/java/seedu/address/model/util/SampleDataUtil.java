package seedu.address.model.util;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.parser.AddAppointmentParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Journal;
import seedu.address.model.ReadOnlyJournal;
import seedu.address.model.journalentry.JournalEntry;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment.Appointment;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.timetable.Timetable;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static ReadOnlyPerson getSamplePerson() {
        return new Person (new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new Timetable("http://modsn.us/56jUQ"),
                new ArrayList<>(getAppointmentList("Lunch, tomorrow 5pm to 7pm")));
    }

    public static JournalEntry[] getSampleJournalEntries() {
        return new JournalEntry[]{
            new JournalEntry("20180205", "Sample Text One"),

            new JournalEntry("20180301", "Sample Text Two"),

            new JournalEntry("20180305", "Sample Text Three")
        };
    }

    public static ReadOnlyJournal getSampleJournal() {
        try {
            Journal sampleJ = new Journal();
            for (JournalEntry sampleJournalEntries : getSampleJournalEntries()) {
                sampleJ.addJournalEntry(sampleJournalEntries);
            }
            return sampleJ;
        } catch (Exception e) {
            throw new AssertionError("sample data cannot contain duplicate journal entries", e);
        }
    }

    /**
     * Returns an Appointment list containing the list of Appointment given
     */
    public static List<Appointment> getAppointmentList(String... strings) {
        List<Appointment> list = new ArrayList<Appointment>();
        for (String s : strings) {
            try {
                list.add(AddAppointmentParser.getAppointmentFromString(s));
            } catch (ParseException e) {
                throw new AssertionError("sample data cannot contain invalid appointments");
            }
        }
        return list;
    }

}
