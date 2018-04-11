package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.JournalChangedEvent;
import seedu.address.commons.events.model.TimetableChangedEvent;
import seedu.address.model.journalentry.JournalEntry;
import seedu.address.model.person.Appointment.Appointment;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final Journal journal;
    private final FilteredList<ReadOnlyPerson> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyJournal journal, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, journal, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.journal = new Journal(journal);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
    }

    public ModelManager() {
        this(new AddressBook(), new Journal(), new UserPrefs());
    }


    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /**
     * Raises an event to indicate the address book model has changed
     */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    //@@author marlenekoh
    /**
     * Raises an event to indicate the timetable storage has changed
     */
    private void indicateTimetableChanged() {
        raise(new TimetableChangedEvent(getFilteredPersonList().get(0).getTimetable()));
    }

    //@@author traceurgan

    /**
     * Raises an event to indicate the journal model has changed
     */
    private void indicateJournalChanged() {
        raise(new JournalChangedEvent(journal));
    }

    @Override
    public synchronized void addPerson(ReadOnlyPerson person) throws DuplicatePersonException {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
        indicateTimetableChanged();
    }

    //@@author traceurgan
    @Override
    public ReadOnlyJournal getJournal() {
        return journal;
    }

    @Override
    public synchronized void addJournalEntry(JournalEntry journalEntry) throws Exception {
        if (journal.getLast().getDate().equals(journalEntry.getDate())) {
            journal.updateJournalEntry(journalEntry, journal.getLast());
        } else {
            journal.addJournalEntry(journalEntry);
            logger.info("journal entry added");
        }
        indicateJournalChanged();
    }

    @Override
    public synchronized void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    //@@author
    @Override
    public void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireAllNonNull(target, editedPerson);

        addressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    //@@author traceurgan
    @Override
    public ObservableList<JournalEntry> getJournalEntryList() {
        return FXCollections.unmodifiableObservableList(journal.getJournalEntryList());
    }

    public JournalEntry getLast() {
        return journal.getLast();
    }

    @Override
    public void addAppointment(ReadOnlyPerson target, Appointment appointments) throws PersonNotFoundException {
        addressBook.addAppointment(target, appointments);
        indicateAddressBookChanged();
    }

    @Override
    public void removeAppointment(ReadOnlyPerson target, Appointment appointment) throws PersonNotFoundException {
        addressBook.removeAppointment(target, appointment);
        indicateAddressBookChanged();
    }
    //@@author
    @Override
    public void updateFilteredPersonList(Predicate<ReadOnlyPerson> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
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
        return addressBook.equals(other.addressBook)
                && filteredPersons.equals(other.filteredPersons);
    }

}
