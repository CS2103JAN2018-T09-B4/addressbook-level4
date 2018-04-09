package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.person.ReadOnlyPerson;

/** Indicates the AddressBook in the model has changed*/
public class PersonChangedEvent extends BaseEvent {

    public final ReadOnlyPerson data;

    public PersonChangedEvent(ReadOnlyPerson data) {
        this.data = data;
    }

    @Override
    public String toString() {
        if (data != null) {
            return "Current person" + data.getName().toString();
        } else {
            return "There is no person saved.";
        }
    }
}
