package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.Journal;
import seedu.address.testutil.TypicalJournalEntries;

//@@author traceurgan
public class XmlSerializableJournalTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath(
            "src/test/data/XmlSerializableJournalTest/");
    private static final File TYPICAL_JOURNALENTRIES_FILE =
            new File(TEST_DATA_FOLDER + "typicalJournalEntriesJournalTest.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalJournalEntriesFile_success() throws Exception {
        XmlSerializableJournal dataFromFile = XmlUtil.getDataFromFile(TYPICAL_JOURNALENTRIES_FILE,
                XmlSerializableJournal.class);
        Journal journalFromFile = dataFromFile.toModelType();
        Journal typicalJournalEntriesJournal = TypicalJournalEntries.getTypicalJournal();
        assertEquals(journalFromFile, typicalJournalEntriesJournal);
    }
}
