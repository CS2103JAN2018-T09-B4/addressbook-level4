package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.Journal;
import seedu.address.model.journalentry.JournalEntry;

/**
 * A utility class containing a list of {@code JournalEntry} objects to be used in tests.
 */
public class TypicalJournalEntries {

    public static final JournalEntry SAMPLE_ONE = new JournalEntry("20180205", "Sample Text One");

    public static final JournalEntry SAMPLE_TWO = new JournalEntry("20180301", "Sample Text Two");

    public static final JournalEntry SAMPLE_THREE = new JournalEntry("20180305", "Sample Text Three");

    private TypicalJournalEntries() {} // prevents instantiation

    /**
     * Returns an {@code Journal} with all the typical journal entries.
     */
    public static Journal getTypicalJournal() {
        Journal j = new Journal();
        for (JournalEntry journalEntry : getTypicalJournalEntries()) {
            try {
                j.addJournalEntry(journalEntry);
            } catch (Exception e) {
                throw new AssertionError("not possible");
            }
        }
        return j;
    }

    public static List<JournalEntry> getTypicalJournalEntries() {
        return new ArrayList<>(Arrays.asList(SAMPLE_ONE, SAMPLE_TWO, SAMPLE_THREE));
    }


}
