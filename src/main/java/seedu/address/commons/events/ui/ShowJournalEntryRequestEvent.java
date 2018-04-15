package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.journalentry.JournalEntry;

/**
 * An event requesting to view a journal entry.
 */
public class ShowJournalEntryRequestEvent extends BaseEvent {


    public final JournalEntry journalEntry;

    public ShowJournalEntryRequestEvent (JournalEntry journalEntry) {
        this.journalEntry = journalEntry;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
