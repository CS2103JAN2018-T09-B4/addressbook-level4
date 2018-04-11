package seedu.address.logic.commands;

import static seedu.address.model.person.Person.PARTNER_INDEX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.model.TimetableChangedEvent;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.timetable.Timetable;
import seedu.address.model.person.timetable.TimetableModuleSlot;
import seedu.address.model.person.timetable.TimetableUtil;

/**
 * Selects your partner from NUSCouples.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";
    public static final String COMMAND_ALIAS = "s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects your partner and shows his/her timetable.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SELECT_PERSON_SUCCESS = "Selected Person: %1$s";

    private final Index targetIndex;

    public SelectCommand() {
        this.targetIndex = Index.fromZeroBased(PARTNER_INDEX);
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson readOnlyPartner = lastShownList.get(targetIndex.getZeroBased());
        Person partner = new Person(readOnlyPartner);

        Timetable timetable = partner.getTimetable();
        ArrayList<TimetableModuleSlot> unsortedModuleSlots =
                TimetableUtil.setUpUnsortedModuleSlotsForViewing(partner.getTimetable());
        timetable.setAllModulesSlots(unsortedModuleSlots);

        HashMap<String, ArrayList<TimetableModuleSlot>> sortedModuleSlots =
                TimetableUtil.sortModuleSlotsByDay(unsortedModuleSlots);
        timetable.setListOfDays(sortedModuleSlots);

        TimetableUtil.setTimetableDisplayInfo(timetable);
        TimetableUtil.setUpTimetableInfo(timetable);

        EventsCenter.getInstance().post(new TimetableChangedEvent(partner.getTimetable()));
        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));

        return new CommandResult(String.format(MESSAGE_SELECT_PERSON_SUCCESS, targetIndex.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }

}
