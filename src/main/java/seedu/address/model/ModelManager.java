package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.JournalChangedEvent;
import seedu.address.commons.events.model.PersonChangedEvent;
import seedu.address.model.journalentry.JournalEntry;
import seedu.address.model.person.Appointment.Appointment;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private Person person;
    private final Journal journal;
    private final ObservableList<ReadOnlyPerson> persons;
    private ObservableList<JournalEntry> journalEntries;


    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyPerson person, ReadOnlyJournal journal, UserPrefs userPrefs) {
        super();
        requireAllNonNull(journal, userPrefs);

        logger.fine(
                "Initializing with partner: " + person + " , journal" + journal + " and user prefs " + userPrefs);

        if (person == null) {
            person = null;
        } else {
            this.person = new Person(person);
        }
        this.journal = new Journal(journal);
        this.persons = FXCollections.observableArrayList();
        this.journalEntries = getJournalEntryList();
        if (person != null) {
            persons.add(person);
        }

    }

    public ModelManager() {
        this(null, new Journal(), new UserPrefs());
    }

    @Override
    public void resetJournalData(ReadOnlyJournal newData) {
        journal.resetJournalData(newData);
        indicateJournalChanged();
    }


    @Override
    public void resetPersonData(ReadOnlyPerson newData) {

    }

    @Override
    public ReadOnlyPerson getPerson() {
        return person;
    }

    @Override
    public ObservableList <ReadOnlyPerson> getPersonAsList() {
        return persons;
    }

    /** Raises an event to indicate the address book model has changed */
    private void indicatePersonChanged(Person person) {
        raise(new PersonChangedEvent(person));
    }

    //@@author traceurgan
    /**
     * Raises an event to indicate the journal model has changed
     */
    private void indicateJournalChanged() {
        raise(new JournalChangedEvent(journal));
    }

    //@@author
    @Override
    public synchronized void deletePerson() throws PersonNotFoundException {
        requireAllNonNull(this.person);
        person = updatePerson(null);
        indicatePersonChanged(person);
    }

    @Override
    public synchronized void addPerson(ReadOnlyPerson newPerson) throws DuplicatePersonException {
        if (this.person != null) {
            throw new DuplicatePersonException();
        }
        requireAllNonNull(newPerson);
        this.person = (Person) newPerson;
        updatePerson(person);
        indicatePersonChanged(person);
    }

    /**
     * Common method for making changes to person.
     * */
    public Person updatePerson(ReadOnlyPerson editedPerson) {
        if (persons.isEmpty()) {
            persons.add(editedPerson);
        } else if (editedPerson == null) {
            persons.remove(0);
        } else {
            persons.set(0, editedPerson);
        }
        return (Person) editedPerson;
    }

    @Override
    public void editPerson(ReadOnlyPerson editedPerson)
            throws NullPointerException {
        requireAllNonNull(this.person, editedPerson);
        person = updatePerson(editedPerson);
        indicatePersonChanged(person);
    }

    //@@author traceurgan
    @Override
    public ReadOnlyJournal getJournal() {
        return journal;
    }

    @Override
    public synchronized void addJournalEntry(JournalEntry journalEntry) throws Exception {
        if (checkDate(journal.getLast()).equals(journalEntry.getDate())) {
            journal.updateJournalEntry(journalEntry, journal.getLast());
            logger.info("Journal entry updated.");
        } else {
            journal.addJournalEntry(journalEntry);
            logger.info("Journal entry added.");
        }
        indicateJournalChanged();
    }

    /**
     * Adds Appointment to a person in the internal list.
     *
     * @throws PersonNotFoundException if no such person exist in the internal list
     */
    public void addAppointment(ReadOnlyPerson target, Appointment appointment) throws PersonNotFoundException {
        requireNonNull(target);
        requireNonNull(appointment);
        Person person = new Person(target);
        List<Appointment> list = target.getAppointments();
        list.add(appointment);
        person.setAppointment(list);
        indicatePersonChanged(person);
    }

    /**
     * Removes an appointment from a person in the internal list
     *
     * @throws PersonNotFoundException if no such person exist in the internal list
     */
    public void removeAppointment(ReadOnlyPerson target, Appointment appointment)
            throws PersonNotFoundException {
        requireNonNull(target);
        requireNonNull(appointment);

        Person person = new Person(target);
        List<Appointment> newApptList = person.getAppointments();
        newApptList.remove(appointment);
        person.setAppointment(newApptList);
        indicatePersonChanged(person);

    }

    @Override
    public String checkDate(int last) {
        return journal.getDate(last);
    }

    //=========== Filtered Journal List Accessors =============================================================


    //@@author traceurgan
    @Override
    public ObservableList<JournalEntry> getJournalEntryList() {
        return journal.getJournalEntryList();
    }

    @Override
    public int getLast() {
        return journal.getLast();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return person.equals(other.person)
                && journal.equals(other.journal);
    }

}
