package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of persons
 */
public class JumpToListRequestEvent extends BaseEvent {

    public final int targetIndex;

    public JumpToListRequestEvent() {
        this.targetIndex = 0;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
