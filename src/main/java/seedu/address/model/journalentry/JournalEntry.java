package seedu.address.model.journalentry;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a JournalEntry in the journal.
 */
public class JournalEntry {

    private final String date;
    private final String text;

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
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(date);
    }
}
