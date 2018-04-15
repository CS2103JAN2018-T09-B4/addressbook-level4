package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalJournalEntries.getTypicalJournal;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

//@@author traceurgan
public class JournalTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final Journal journal = new Journal();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), journal.getJournalEntryList());
    }
    @Test
    public void resetJournalData_nullData() throws Exception {
        thrown.expect(NullPointerException.class);
        journal.resetJournalData(null);
    }

    @Test
    public void resetData_withValidReadOnlyJournal_replacesData() {
        Journal newData = getTypicalJournal();
        journal.resetJournalData(newData);
        assertEquals(newData, journal);
    }

    @Test
    public void getJournalEntryList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        journal.getJournalEntryList().remove(0);
    }
}
