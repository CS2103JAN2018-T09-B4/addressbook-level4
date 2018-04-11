package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.person.timetable.Timetable;

//@@author marlenekoh
/** Indicates the Timetable of the partner has changed*/
public class TimetableChangedEvent extends BaseEvent {
    public final Timetable timetable;

    public TimetableChangedEvent(Timetable timetable) {
        this.timetable = timetable;
    }

    @Override
    public String toString() { return null; }
}
