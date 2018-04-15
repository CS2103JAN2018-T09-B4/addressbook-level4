package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_JOURNAL;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.journalentry.Date;

/**
 * Shows a journal entry from a date
 */
public class ViewJournalCommand extends Command {

    public static final String COMMAND_WORD = "jview";
    public static final String COMMAND_ALIAS = "jv";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects a journal entry by date and view its contents. \n"
            + "Parameters: "
            + PREFIX_JOURNAL + "DATE\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_JOURNAL + "20180414\n"
            + "This opens the journal entry created on the 14th of February 2018.";

    public static final String MESSAGE_VIEW_JOURNAL_ENTRY_SUCCESS = "Showing journal entry: %1$s";
    public static final String MESSAGE_JOURNAL_ENTRY_NOT_FOUND =
            "There is no journal entry written on: %1$s.\n"
            + "Use the jnew command to add a new journal entry.";

    private Date date;

    public ViewJournalCommand(Date date) {
        this.date = date;
    }

    @Override
    public CommandResult execute() throws CommandException {
        try {
            model.viewJournalEntry(date);

        } catch (Exception e) {
            throw new CommandException(String.format(MESSAGE_JOURNAL_ENTRY_NOT_FOUND, this.date));
        }
        return new CommandResult(String.format(MESSAGE_VIEW_JOURNAL_ENTRY_SUCCESS, this.date));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewJournalCommand); // instanceof handles nulls
    }

}
