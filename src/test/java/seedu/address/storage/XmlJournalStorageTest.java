package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalJournalEntries.TEST;
import static seedu.address.testutil.TypicalJournalEntries.getTypicalJournal;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.Journal;
import seedu.address.model.ReadOnlyJournal;

//@@author traceurgan
public class XmlJournalStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlJournalStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private java.util.Optional<ReadOnlyJournal> readJournal(String filePath) throws Exception {
        return new XmlJournalStorage(filePath).readJournal(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void readJournal_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readJournal(null);

    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readJournal("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readJournal("NotXmlFormatJournal.xml");
    }

    @Test
    public void readAndSaveJournal_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempJournal.xml";
        Journal original = getTypicalJournal();
        XmlJournalStorage xmlJournalStorage = new XmlJournalStorage(filePath);

        //Save in new file and read back
        xmlJournalStorage.saveJournal(original, filePath);
        ReadOnlyJournal readBack = xmlJournalStorage.readJournal(filePath).get();
        assertEquals(original, new Journal(readBack));

        //Save and read without specifying file path
        original.addJournalEntry(TEST);
        xmlJournalStorage.saveJournal(original); //file path not specified
        readBack = xmlJournalStorage.readJournal().get(); //file path not specified
        assertEquals(original, new Journal(readBack));

    }

    @Test
    public void saveJournal_nullJournal_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveJournal(null, "SomeFile.xml");
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveJournal(ReadOnlyJournal journal, String filePath) {
        try {
            new XmlJournalStorage(filePath).saveJournal(journal, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveJournal_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveJournal(new Journal(), null);
    }


}
