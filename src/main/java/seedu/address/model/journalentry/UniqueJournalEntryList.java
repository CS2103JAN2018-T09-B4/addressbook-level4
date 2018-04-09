package seedu.address.model.journalentry;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//@@author traceurgan
/**
 * A list of journal entries that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see JournalEntry#equals(Object)
 */
public class UniqueJournalEntryList implements Iterable <JournalEntry> {

    private final ObservableList<JournalEntry> internalList = FXCollections.observableArrayList();
    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(JournalEntry toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Returns last entry
     */
    public int getLast() {
        return (internalList.size() - 1);
    }

    /**
     * Adds a person to the list.
     *
     * @throws Exception if the person to add is a duplicate of an existing person in the list.
     */
    public void add(JournalEntry toAdd) throws Exception {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new Exception();
        }
        internalList.add(toAdd);
    }

    /**
    * Replaces the journal entry {@code target} in the list with {@code editedPerson}.
    *
    * @throws Exception if the replacement is equivalent to another existing journal entry in the list.
    * @throws Exception if {@code target} could not be found in the list.
    */
    public void setJournalEntries(UniqueJournalEntryList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setJournalEntries(List<JournalEntry> journalEntries) throws Exception {
        requireAllNonNull(journalEntries);
        final UniqueJournalEntryList replacement = new UniqueJournalEntryList();
        for (final JournalEntry journalEntry : journalEntries) {
            replacement.add(journalEntry);
        }
        setJournalEntries(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<JournalEntry> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<JournalEntry> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueJournalEntryList // instanceof handles nulls
                && this.internalList.equals(((UniqueJournalEntryList) other).internalList));
    }

    public void updateJournalEntry(JournalEntry journalEntry, int last) {
        internalList.set(last, journalEntry);
    }

    public String getDate(int last) {
        return internalList.get(last).getDate();
    }

    public JournalEntry getJournalEntry(int index) {
        return internalList.get(index);
    }
}
