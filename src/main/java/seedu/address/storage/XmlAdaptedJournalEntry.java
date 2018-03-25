package seedu.address.storage;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.journalentry.JournalEntry;

/**
 * JAXB-friendly version of the JournalEntry.
 */
public class XmlAdaptedJournalEntry {

    @XmlElement(required = true)
    private String date;
    @XmlElement(required = true)
    private String text;

    /**
     * Constructs an XmlAdaptedJournalEntry.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedJournalEntry() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedJournalEntry(String date, String text) {
        this.date = date;
        this.text = text;
    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedJournalEntry(JournalEntry source) {
        date = source.getDate();
        text = source.getText();
    }

    /**
     * Converts this jaxb-friendly adapted journal entry object into the model's JournalEntry object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted journal entry
     */
    public JournalEntry toModelType() throws IllegalValueException {

        if (this.date == null) { //impossible, date is generated when new journal entry is created
            throw new IllegalValueException("Date missing");
        }

        return new JournalEntry(date, text);
    }

}
