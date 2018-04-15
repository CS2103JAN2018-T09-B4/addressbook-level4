package seedu.address.storage;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.journalentry.Date;
import seedu.address.model.journalentry.JournalEntry;

//@@author traceurgan
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
     * Constructs an {@code XmlAdaptedJournalEntry} with the given journal entry details.
     */
    public XmlAdaptedJournalEntry(String date, String text) {
        this.date = date;
        this.text = text;
    }

    /**
     * Converts a given Journal Entry into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedJournalEntry(JournalEntry source) {
        date = source.getDate().value;
        text = source.getText();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedJournalEntry)) {
            return false;
        }

        XmlAdaptedJournalEntry otherJournalEntry = (XmlAdaptedJournalEntry) other;
        return this.date.equals(otherJournalEntry.date)
                && this.text.equals(otherJournalEntry.text);
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
        final Date date = new Date(this.date);

        return new JournalEntry(date, text);
    }

}
