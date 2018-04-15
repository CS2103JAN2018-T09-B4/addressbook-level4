package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOURNAL;

import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ViewJournalCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.journalentry.Date;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class ViewJournalCommandParser implements Parser<ViewJournalCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewJournalCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_JOURNAL);

        if (!arePrefixesPresent(argMultimap, PREFIX_JOURNAL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewJournalCommand.MESSAGE_USAGE));
        }

        try {
            Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_JOURNAL)).get();

            return new ViewJournalCommand(date);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
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
