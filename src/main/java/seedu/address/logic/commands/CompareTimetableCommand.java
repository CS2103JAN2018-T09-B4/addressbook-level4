package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMETABLE;
import static seedu.address.model.person.Person.PARTNER_INDEX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.model.TimetableChangedEvent;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.timetable.Timetable;
import seedu.address.model.person.timetable.TimetableModuleSlot;
import seedu.address.model.person.timetable.TimetableUtil;

/**
 * Compares the partner's timetable with a given timetable
 */
public class CompareTimetableCommand extends Command {

    public static final String COMMAND_WORD = "tcompare";
    public static final String COMMAND_ALIAS = "tc";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Compares your partner's timetable with a given timetable.\n"
            + "Parameters: " + PREFIX_TIMETABLE + "TIMETABLE_URL\n"
            + "Example: " + COMMAND_WORD + " http://modsn.us/wNuIW";

    public static final String MESSAGE_TIMETABLE_COMPARE_SUCCESS = "Compared timetable";

    private Timetable otherTimetable;

    public CompareTimetableCommand(Timetable otherTimetable) {
        this.otherTimetable = otherTimetable;
    }

    @Override
    public CommandResult execute() {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
        ReadOnlyPerson readOnlyPartner = lastShownList.get(PARTNER_INDEX);
        Person partner = new Person(readOnlyPartner);

        ArrayList<TimetableModuleSlot> unsortedModuleSlots =
                TimetableUtil.setUpUnsortedModuleSlotsForComparing(partner.getTimetable(), otherTimetable);
        HashMap<String, ArrayList<TimetableModuleSlot>> sortedModuleSlots =
                TimetableUtil.sortModuleSlotsByDay(unsortedModuleSlots);
        otherTimetable.setListOfDays(sortedModuleSlots);
        TimetableUtil.setTimetableDisplayInfo(otherTimetable);

        EventsCenter.getInstance().post(new TimetableChangedEvent(otherTimetable));
        EventsCenter.getInstance().post(new JumpToListRequestEvent(Index.fromZeroBased(PARTNER_INDEX)));

        return new CommandResult(MESSAGE_TIMETABLE_COMPARE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CompareTimetableCommand // instanceof handles nulls
                && this.otherTimetable.equals(((CompareTimetableCommand) other).otherTimetable)); // state check
    }
}
