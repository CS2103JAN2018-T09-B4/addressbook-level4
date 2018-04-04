package seedu.address.logic.commands;

//@@author marlenekoh

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ReloadTimetableRequestEvent;

/**
 * Displays Timetable of the specified person in browser panel
 */
public class ViewTimetableCommand extends Command {

    public static final String COMMAND_WORD = "tview";
    public static final String COMMAND_ALIAS = "tv";

    public static final String MESSAGE_SUCCESS = "Timetable displayed";

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ReloadTimetableRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
