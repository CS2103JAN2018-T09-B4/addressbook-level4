package seedu.address.logic;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.SaveEntryEvent;
import seedu.address.commons.events.ui.ShowJournalWindowRequestEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.journalentry.JournalEntry;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.ui.JournalWindow;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final AddressBookParser addressBookParser;
    private final UndoRedoStack undoRedoStack;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        addressBookParser = new AddressBookParser();
        undoRedoStack = new UndoRedoStack();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = addressBookParser.parseCommand(commandText);
            command.setData(model, history, undoRedoStack);
            CommandResult result = command.execute();
            undoRedoStack.push(command);
            return result;
        } finally {
            history.add(commandText);
        }
    }

    //@@author traceurgan
    @Subscribe
    public void handleSaveEntryEvent(SaveEntryEvent event) {
        try {
            model.addJournalEntry(event.journalEntry);
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
        if (model.checkDate(model.getLast()).equals(event.date)) {
            journalWindow = new JournalWindow(
                    event.date, model.getJournal().getJournalEntry(model.getLast()).getText());
        } else {
            journalWindow = new JournalWindow(event.date);
        }
        journalWindow.show();
    }

    //@@author traceurgan
    @Override
    public ObservableList<JournalEntry> getJournalEntryList() {
        return model.getJournalEntryList();
    }

    @Override
    public ObservableList<ReadOnlyPerson> getPersonAsList() {
        return model.getPersonAsList();
    }

    //@@author
    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }
}
