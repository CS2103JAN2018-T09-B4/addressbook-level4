package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Journal;

/**
 * Clears the journal.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String COMMAND_ALIAS = "c";
    public static final String MESSAGE_SUCCESS = "Journal has been cleared!";

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        model.resetJournalData(new Journal());
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
