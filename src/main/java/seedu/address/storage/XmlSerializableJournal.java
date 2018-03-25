package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Journal;
import seedu.address.model.ReadOnlyJournal;

/**
 * An Immutable Journal that is serializable to XML format
 */
@XmlRootElement(name = "journal")
public class XmlSerializableJournal {

    @XmlElement
    private List<XmlAdaptedJournalEntry> journalEntries;

    /**
     * Creates an empty XmlSerializableJournal.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableJournal() {
        journalEntries = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableJournal (ReadOnlyJournal src) {
        this();
        journalEntries.addAll(src.getJournalEntryList().stream().map(
                XmlAdaptedJournalEntry::new).collect(Collectors.toList()));
    }

    /**
     * Converts this journal into the model's {@code Journal} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedJournalEntry}.
     */
    public Journal toModelType() throws Exception {
        Journal journal = new Journal();

        for (XmlAdaptedJournalEntry p : journalEntries) {
            journal.addJournalEntry(p.toModelType());
        }
        return journal;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableJournal)) {
            return false;
        }

        XmlSerializableJournal otherJournal = (XmlSerializableJournal) other;
        return journalEntries.equals(otherJournal.journalEntries);
    }
}
