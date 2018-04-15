package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.JournalChangedEvent;
import seedu.address.commons.events.model.PersonChangedEvent;
import seedu.address.commons.events.model.SaveEntryEvent;
import seedu.address.commons.events.model.TimetableChangedEvent;
import seedu.address.commons.events.ui.HideTimetableRequestEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.commons.events.ui.ShowJournalEntryRequestEvent;
import seedu.address.commons.events.ui.ShowJournalWindowRequestEvent;
import seedu.address.commons.events.ui.ShowTimetableRequestEvent;
import seedu.address.model.journalentry.Date;
import seedu.address.model.journalentry.JournalEntry;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.appointment.Appointment;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.timetable.Timetable;
import seedu.address.model.person.timetable.TimetableUtil;
import seedu.address.ui.JournalWindow;

/**
 * Represents the in-memory model of the NUSCouples data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private Person partner;
    private final Journal journal;
    private final ObservableList<ReadOnlyPerson> persons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyPerson partner, ReadOnlyJournal journal, UserPrefs userPrefs) {
        super();
        requireAllNonNull(journal, userPrefs);

        logger.fine(
                "Initializing with partner: " + partner + " , journal" + journal + " and user prefs " + userPrefs);

        if (partner == null) {
            partner = null;
        } else {
            this.partner = new Person(partner);
        }
        this.journal = new Journal(journal);
        this.persons = FXCollections.observableArrayList();
        if (partner != null) {
            persons.add(partner);
        }

    }

    public ModelManager() {
        this(null, new Journal(), new UserPrefs());
    }

    @Override
    public void resetJournalData(ReadOnlyJournal newData) {
        journal.resetJournalData(newData);
        indicateJournalChanged();
        requestHideTimetable();
    }

    @Override
    public void resetPersonData(ReadOnlyPerson newData) {
        partner = new Person(newData);
        updatePerson(partner);
        indicatePersonChanged(partner);
        indicateTimetableChanged(partner.getTimetable());
    }

    @Override
    public ReadOnlyPerson getPartner() {
        return partner;
    }

    @Override
    public ObservableList <ReadOnlyPerson> getPersonAsList() {
        return persons;
    }

    /** Raises an event to indicate the address book model has changed */
    private void indicatePersonChanged(Person person) {
        raise(new PersonChangedEvent(person));
    }

    //@@author marlenekoh
    @Override
    public void indicateTimetableChanged(Timetable timetable) {
        raise(new TimetableChangedEvent(timetable));
    }

    @Override
    public void requestHideTimetable() {
        raise(new HideTimetableRequestEvent());
    }

    @Override
    public void requestShowTimetable() {
        raise(new ShowTimetableRequestEvent());
    }

    @Override
    public int getLast() {
        return journal.getLast();
    }

    //@@author
    @Override
    public synchronized void deletePerson() throws NullPointerException {
        if (partner == null) {
            throw new NullPointerException();
        }
        partner = updatePerson(null);
        indicatePersonChanged(partner);
        requestHideTimetable();
    }

    @Override
    public synchronized void addPerson(ReadOnlyPerson newPerson) throws DuplicatePersonException {
        if (this.partner != null) {
            throw new DuplicatePersonException();
        }
        requireAllNonNull(newPerson);
        this.partner = (Person) newPerson;
        updatePerson(partner);
        indicatePersonChanged(partner);
        indicateTimetableChanged(partner.getTimetable());
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
        requireAllNonNull(this.partner, editedPerson);
        partner = updatePerson(editedPerson);
        indicatePersonChanged(partner);
        indicateTimetableChanged(partner.getTimetable());
    }

    //@@author marlenekoh
    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        TimetableUtil.setUpTimetableInfoView(getPartner().getTimetable());
        indicateTimetableChanged(getPartner().getTimetable());
        raise(new ShowTimetableRequestEvent());
    }

    //@@author traceurgan
    @Subscribe
    public void handleSaveEntryEvent(SaveEntryEvent event) {
        try {
            addJournalEntry(event.journalEntry);
        } catch (Exception e) {
            logger.warning("Save failed");
            JournalWindow journalWindow =
                    new JournalWindow(event.journalEntry.getDate(), String.format(
                            "Save failed. Copy your text and try again.\n" + event.journalEntry.getText()));
            journalWindow.show();
        }
    }

    @Subscribe
    private void handleShowJournalWindowRequestEvent(ShowJournalWindowRequestEvent event) {
        JournalWindow journalWindow;
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if ((getJournalEntryList().size() != 0) && (contains(event.date))) {
            journalWindow = new JournalWindow(
                    event.date, getJournal().getJournalEntry(event.date).getText());
        } else {
            journalWindow = new JournalWindow(event.date);
        }
        journalWindow.show();
    }
    /**
     * Adds appointment to a person in the internal list.
     *
     * @throws PersonNotFoundException if no such person exist in the internal list
     */
    public void addAppointment(ReadOnlyPerson target, Appointment appointment) throws PersonNotFoundException {
        requireNonNull(target);
        requireNonNull(appointment);
        Person person = (Person) getPartner();
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

        Person person = (Person) getPartner();
        List<Appointment> newApptList = person.getAppointments();
        newApptList.remove(appointment);
        person.setAppointment(newApptList);
        indicatePersonChanged(person);

    }

    //=========== Journal Methods =============================================================

    //@@author traceurgan
    @Override
    public ObservableList<JournalEntry> getJournalEntryList() {
        return journal.getJournalEntryList();
    }

    //@@author traceurgan
    /**
     * Raises an event to indicate the journal model has changed.
     */
    private void indicateJournalChanged() {
        raise(new JournalChangedEvent(journal));
    }

    @Override
    public boolean contains(Date date) {
        return journal.containsJournalEntry(date);
    }

    //@@author traceurgan
    @Override
    public ReadOnlyJournal getJournal() {
        return journal;
    }

    @Override
    public synchronized void addJournalEntry(JournalEntry journalEntry) throws Exception {
        if ((this.getJournalEntryList().size() != 0) && (
                contains(journalEntry.getDate()))) {
            journal.updateJournalEntry(journalEntry, journal.getLast());
            logger.info("Journal entry updated.");
        } else {
            journal.addJournalEntry(journalEntry);
            logger.info("Journal entry added.");
        }
        indicateJournalChanged();
    }

    @Override
    public JournalEntry getJournalEntry(Date date) {
        return journal.getJournalEntry(date);
    }

    @Override
    public void viewJournalEntry(Date date) throws Exception {
        if (contains(date)) {
            raise (new ShowJournalEntryRequestEvent(journal.getJournalEntry(date)));
        } else {
            throw new Exception();
        }
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
        return ((partner == null && other.partner == null)
                || (partner != null && other.partner != null && partner.equals(other.partner)))
                && journal.equals(other.journal);
    }

}
