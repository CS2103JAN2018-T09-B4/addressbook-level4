package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.journalentry.JournalEntry;
import seedu.address.model.person.Appointment.Appointment;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {

    /** Clears existing backing model for journal and replaces with the provided new data. */
    void resetJournalData(ReadOnlyJournal newData);

    /** Clears existing backing model for person and replaces with the provided new data. */
    void resetPersonData(ReadOnlyPerson newData);

    /** Returns the Person */
    ReadOnlyPerson getPerson();

    /** Returns the Person as Observable list */
    ObservableList<ReadOnlyPerson> getPersonAsList();

    //@@author traceurgan
    /** Returns the Journal */
    ReadOnlyJournal getJournal();

    /** Adds the given person */
    void addJournalEntry(JournalEntry journalEntry) throws Exception;

    /** Returns an unmodifiable view of the journal entry list */
    ObservableList<JournalEntry> getJournalEntryList();

    //@@author
    void deletePerson() throws PersonNotFoundException;

    /** Adds the given person */
    void addPerson(ReadOnlyPerson person) throws DuplicatePersonException;

    /** Edits the given person
     *
     * @throws NullPointerException
     *
     * */
    void editPerson(ReadOnlyPerson editedPerson)
            throws NullPointerException;

    String checkDate(int last);

    Person updatePerson(ReadOnlyPerson editedPerson)
            throws DuplicatePersonException, PersonNotFoundException;

    //@@author chenxing1992
    /**
     * Adds Appointment to a person
     */
    void addAppointment(ReadOnlyPerson target, Appointment appointment) throws PersonNotFoundException;

    /**
     * Removes appointment from a person
     */
    void removeAppointment(ReadOnlyPerson target, Appointment appointment) throws PersonNotFoundException;

    int getLast();
}
