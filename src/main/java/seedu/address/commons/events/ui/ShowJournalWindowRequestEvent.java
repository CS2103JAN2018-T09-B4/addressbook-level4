package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.journalentry.Date;

//@@author traceurgan
/**
 * An event requesting to view the Journal Window.
 */
public class ShowJournalWindowRequestEvent extends BaseEvent {

    public final Date date;

    public ShowJournalWindowRequestEvent (Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
