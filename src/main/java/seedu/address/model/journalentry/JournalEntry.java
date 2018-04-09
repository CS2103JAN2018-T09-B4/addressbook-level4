package seedu.address.model.journalentry;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

//@@author traceurgan
/**
 * Represents a JournalEntry in the journal.
 */
public class JournalEntry {

    private final String date;
    private String text;

    /**
     * Every field must be present and not null.
     */
    public JournalEntry(String date, String text) {
        requireAllNonNull(date, text);
        this.date = date;
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof JournalEntry)) {
            return false;
        }

        JournalEntry otherJournalEntry = (JournalEntry) other;
        return otherJournalEntry.getDate().equals(this.getDate())
                && otherJournalEntry.getText().equals(this.getText());
    }

    private void setText(JournalEntry updatedVersion) {
        this.text = updatedVersion.getText();
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(date);
    }

    static void updateJournalEntry(JournalEntry journalEntry, JournalEntry oldVersion) {
        oldVersion.setText(journalEntry);
    }
}
