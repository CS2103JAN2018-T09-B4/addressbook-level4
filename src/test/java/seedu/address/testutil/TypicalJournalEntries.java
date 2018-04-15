package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.Journal;
import seedu.address.model.journalentry.Date;
import seedu.address.model.journalentry.JournalEntry;

//@@author traceurgan
/**
 * A utility class containing a list of {@code JournalEntry} objects to be used in tests.
 */
public class TypicalJournalEntries {

    public static final JournalEntry SAMPLE_ONE = new JournalEntry(new Date("20180101"), "Sample text here.");

    public static final JournalEntry SAMPLE_TWO = new JournalEntry(new Date("20181001"), "Sample two here.");

    public static final JournalEntry SAMPLE_THREE = new JournalEntry(new Date("20181111"), "Sample three text.");

    //for manual adding during tests
    public static final JournalEntry TEST = new JournalEntry(new Date("20180328"), "Testing.");

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
