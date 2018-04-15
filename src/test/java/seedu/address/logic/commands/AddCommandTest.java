package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyJournal;
import seedu.address.model.journalentry.Date;
import seedu.address.model.journalentry.JournalEntry;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.appointment.Appointment;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.timetable.Timetable;
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        seedu.address.model.person.Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = getAddCommandForPerson(validPerson, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validPerson), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePersonException();
        seedu.address.model.person.Person validPerson = new PersonBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_MULTIPLE_PERSON);

        getAddCommandForPerson(validPerson, modelStub).execute();
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        // assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given person.
     */
    private AddCommand getAddCommandForPerson(seedu.address.model.person.Person person, Model model) {
        AddCommand command = new AddCommand(person);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(ReadOnlyPerson person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void editPerson(ReadOnlyPerson editedPerson) throws NullPointerException {
            fail("This method should not be called.");
        }

        @Override
        public Person updatePerson(ReadOnlyPerson editedPerson) {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void addJournalEntry(JournalEntry journalEntry) throws Exception {
            fail("This method should not be called.");
        }

        @Override
        public boolean contains(Date date) {
            fail("This method should not be called.");
            return false;
        }

        //@@author chenxing1992
        @Override
        public void addAppointment(ReadOnlyPerson person, Appointment appointment) throws PersonNotFoundException {
            fail("This method should not be called");
        }

        //@@author chenxing1992
        @Override
        public void removeAppointment(ReadOnlyPerson target, Appointment appointment) throws PersonNotFoundException {
            fail("This method should not be called");
        }

        @Override
        public void indicateTimetableChanged(Timetable timetable) {
            fail("This method should not be called.");
        }

        @Override
        public void requestHideTimetable() {
            fail("This method should not be called.");
        }

        @Override
        public void requestShowTimetable() {
            fail("This method should not be called.");
        }

        @Override
        public int getLast() {
            fail("This method should not be called.");
            return 0;
        }

        @Override
        public JournalEntry getJournalEntry(Date date) {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void viewJournalEntry(Date date) throws Exception {
            fail("This method should not be called.");
        }

        @Override
        public void resetPersonData(ReadOnlyPerson newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyPerson getPartner() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void resetJournalData(ReadOnlyJournal newData) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<ReadOnlyPerson> getPersonAsList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ReadOnlyJournal getJournal() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<JournalEntry> getJournalEntryList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson() {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicatePersonException when trying to add a person.
     */
    private class ModelStubThrowingDuplicatePersonException extends ModelStub {
        @Override
        public void addPerson(seedu.address.model.person.ReadOnlyPerson person) throws DuplicatePersonException {
            throw new DuplicatePersonException();
        }

        @Override
        public ReadOnlyPerson getPartner() {
            return new Person(ALICE);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<ReadOnlyPerson> personsAdded = new ArrayList<>();

        @Override
        public void addPerson(ReadOnlyPerson person) throws DuplicatePersonException {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyPerson getPartner() {
            return new Person(ALICE);
        }
    }

}
