package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMETABLE;

import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.CompareTimetableCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.timetable.Timetable;

/**
 * Parses input arguments and creates a new CompareTimetableCommand
 */
public class CompareTimetableCommandParser implements Parser<CompareTimetableCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the CompareTimetableCommand
     * and returns an CompareTimetableCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CompareTimetableCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TIMETABLE);

        if (!arePrefixesPresent(argMultimap, PREFIX_TIMETABLE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    CompareTimetableCommand.MESSAGE_USAGE));
        }

        try {
            Timetable timetable = ParserUtil.parseTimetable(argMultimap.getValue(PREFIX_TIMETABLE)).get();
            return new CompareTimetableCommand(timetable);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompareTimetableCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
