package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddAppointmentCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CalendarViewCommand;
import seedu.address.logic.commands.CancelAppointmentCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.NewJournalCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());

        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        return getCommand(commandWord, arguments);
    }

    private Command getCommand(String commandWord, String arguments) throws ParseException {
        switch (commandWord) {
        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_ALIAS: //@@author HEARTOFAL1ON
            return new AddCommandParser().parse(arguments); //@@author

        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_ALIAS: //@@author HEARTOFAL1ON
            return new EditCommandParser().parse(arguments); //@@author

        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS: //@@author HEARTOFAL1ON
            return new SelectCommand(); //@@author

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_ALIAS: //@@author HEARTOFAL1ON
            return new DeleteCommand(); //@@author

        case ClearCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_ALIAS: //@@author HEARTOFAL1ON
            return new ClearCommand(); //@@author

        case HistoryCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_ALIAS: //@@author HEARTOFAL1ON
            return new HistoryCommand(); //@@author

        case ExitCommand.COMMAND_WORD:
        case ExitCommand.COMMAND_ALIAS: //@@author HEARTOFAL1ON
            return new ExitCommand(); //@@author

        case NewJournalCommand.COMMAND_WORD:
        case NewJournalCommand.COMMAND_ALIAS: //@@author HEARTOFAL1ON
            return new NewJournalCommand(); //@@author

        case HelpCommand.COMMAND_WORD:
        case HelpCommand.COMMAND_ALIAS: //@@author HEARTOFAL1ON
            return new HelpCommand(); //@@author

        case UndoCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_ALIAS: //@@author HEARTOFAL1ON
            return new UndoCommand(); //@@author

        case RedoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_ALIAS: //@@author HEARTOFAL1ON
            return new RedoCommand(); //@@author

        case CalendarViewCommand.COMMAND_WORD:
        case CalendarViewCommand.COMMAND_ALIAS:
            return new CalendarViewParser().parse(arguments);

        case CancelAppointmentCommand.COMMAND_WORD:
            return new CancelAppointmentParser().parse(arguments);

        case AddAppointmentCommand.COMMAND_WORD:
        case AddAppointmentCommand.COMMAND_ALIAS:
            return new AddAppointmentParser().parse(arguments);
        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);

        }
    }

}
