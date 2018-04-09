package seedu.address.storage;

import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * An Immutable Person that is serializable to XML format
 */
@XmlRootElement(name = "person")
public class XmlSerializablePerson {

    @XmlElement
    private ArrayList<XmlAdaptedPerson> persons;

    /**
     * Creates an empty XmlSerializablePerson.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializablePerson() {
        persons = new ArrayList<XmlAdaptedPerson>();
    }

    /**
     * Conversion
     */
    public XmlSerializablePerson(ReadOnlyPerson src) {
        this();
        persons.add(new XmlAdaptedPerson(src));
    }

    /**
     * Converts this addressbook into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedPerson} or {@code XmlAdaptedTag}.
     */
    public Person toModelType() throws IllegalValueException, ParseException {
        return persons.get(0).toModelType();
    }

    public int getSize() {
        return persons.size();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializablePerson)) {
            return false;
        }

        XmlSerializablePerson xsp = (XmlSerializablePerson) other;
        if (persons.size() != 0) {
            return persons.get(0).equals(xsp.persons.get(0));
        } else {
            return persons.size() == (xsp.persons.size());
        }
    }
}
