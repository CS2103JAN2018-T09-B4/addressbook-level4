package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyJournal;

/** Indicates the Journal in the model has changed*/
public class JournalChangedEvent extends BaseEvent {

    public final ReadOnlyJournal data;

    public JournalChangedEvent(ReadOnlyJournal data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of journal entries " + data.getJournalEntryList().size();
    }
}
