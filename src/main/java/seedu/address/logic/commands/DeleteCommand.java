package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Deletes a person identified using it's last displayed index from the address book.
 */
public class DeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_ALIAS = "d";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person shown in the main window.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private ReadOnlyPerson personToDelete;

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(personToDelete);
        return getCommandResult();
    }

    private CommandResult getCommandResult() {
        try {
            model.deletePerson();
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("There is no one to delete.");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        try {
            this.personToDelete = model.getPerson();
        } catch (NullPointerException npe) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && Objects.equals(this.personToDelete, ((DeleteCommand) other).personToDelete));
    }

}
