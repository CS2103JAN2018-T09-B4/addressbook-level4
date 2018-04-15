package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.journalentry.Date;
import seedu.address.model.journalentry.JournalEntry;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.appointment.Appointment;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.timetable.Timetable;

/**
 * The API of the Model component.
 */
public interface Model {

    /** Clears existing backing model for journal and replaces with the provided new data. */
    void resetJournalData(ReadOnlyJournal newData);

    /** Clears existing backing model for person and replaces with the provided new data. */
    void resetPersonData(ReadOnlyPerson newData);

    /** Returns the Person */
    ReadOnlyPerson getPartner();

    //@@author traceurgan
    /** Returns the Journal */
    ReadOnlyJournal getJournal();

    /** Adds the given person */
    void addJournalEntry(JournalEntry journalEntry) throws Exception;

    boolean contains(Date date);

    /** Returns an unmodifiable view of the journal entry list */
    ObservableList<JournalEntry> getJournalEntryList();

    ObservableList <ReadOnlyPerson> getPersonAsList();

    //@@author
    void deletePerson() throws NullPointerException;

    /** Adds the given person */
    void addPerson(ReadOnlyPerson person) throws DuplicatePersonException;

    /** Edits the given person */
    void editPerson(ReadOnlyPerson editedPerson) throws NullPointerException;

    /** Updates the given person */
    Person updatePerson(ReadOnlyPerson editedPerson);

    //@@author chenxing1992
    /**
     * Adds appointment to a person
     */
    void addAppointment(ReadOnlyPerson target, Appointment appointment) throws PersonNotFoundException;

    /**
     * Removes appointment from a person
     */
    void removeAppointment(ReadOnlyPerson target, Appointment appointment) throws PersonNotFoundException;

    //@@author marlenekoh
    /**
     * Raises an event to indicate the timetable storage has changed.
     */
    void indicateTimetableChanged(Timetable timetable);
    /**
     * Raises an event to hide timetable.
     */
    void requestHideTimetable();

    /**
     * Raises an event to show timetable.
     */
    void requestShowTimetable();

    //@@author
    int getLast();

    JournalEntry getJournalEntry(Date date);

    /**
     * Opens an existing journal entry
     * @param date
     */
    void viewJournalEntry(Date date) throws Exception;
}
