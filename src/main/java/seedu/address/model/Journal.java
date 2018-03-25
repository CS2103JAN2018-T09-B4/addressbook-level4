package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.journalentry.JournalEntry;
import seedu.address.model.journalentry.UniqueJournalEntryList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class Journal implements ReadOnlyJournal {

    private static final Logger logger = LogsCenter.getLogger(Journal.class);

    private final UniqueJournalEntryList journalEntries;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        journalEntries = new UniqueJournalEntryList();
    }

    public Journal() {

    }

    /**
     * Creates an Journal using the Journal Entries {@code toBeCopied}
     */
    public Journal(ReadOnlyJournal toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyJournal newData) {
        requireNonNull(newData);
        List<JournalEntry> syncedJournalEntryList = newData.getJournalEntryList().stream()
                .collect(Collectors.toList());

        try {
            setJournalEntries(syncedJournalEntryList);
        } catch (Exception e) {
            throw new AssertionError("Journal should not have duplicate entries");
        }
    }

    //// person-level operations

    /**
     * Adds a journal entry to the journal.
     *
     * @throws Exception if an equivalent journal entry already exists.
     */
    public void addJournalEntry(JournalEntry j) throws Exception {
        journalEntries.add(j);
    }

    public void setJournalEntries(List<JournalEntry> journalEntries) throws Exception {
        this.journalEntries.setJournalEntries(journalEntries);
    }


    //// util methods

    @Override
    public ObservableList<JournalEntry> getJournalEntryList() {
        return journalEntries.asObservableList();
    }

    public void getList () {
        for (JournalEntry journalEntry : journalEntries) {
            logger.info(journalEntry.getDate());
        }
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.journalEntries.equals(((Journal) other).journalEntries));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(journalEntries);
    }
}
