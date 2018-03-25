package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyJournal;

/**
 * A class to access AddressBook data stored as an xml file on the hard disk.
 */
public class XmlJournalStorage implements JournalStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlJournalStorage.class);

    private String filePath;

    public XmlJournalStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getJournalFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyJournal> readJournal() throws DataConversionException, IOException {
        return readJournal(filePath);
    }

    /**
     * Similar to {@link #readJournal()} ()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyJournal> readJournal(String filePath) throws DataConversionException,
            IOException {
        requireNonNull(filePath);

        File journalFile = new File(filePath);

        if (!journalFile.exists()) {
            logger.info("Journal file "  + journalFile  + " not found");
            return Optional.empty();
        }

        XmlSerializableJournal xmlJournal = XmlFileStorage.loadJournalFromSaveFile(new File(filePath));
        try {
            return Optional.of(xmlJournal.toModelType());
        } catch (Exception e) {
            logger.info("Illegal values found in " + journalFile + ": " + e.getMessage());
            throw new DataConversionException(e);
        }
    }

    @Override
    public void saveJournal(ReadOnlyJournal journal) throws IOException {
        saveJournal(journal, filePath);
    }

    /**
     * Similar to {@link #saveJournal(ReadOnlyJournal)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveJournal(ReadOnlyJournal journal, String filePath) throws IOException {
        requireNonNull(journal);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveJournalToFile(file, new XmlSerializableJournal(journal));
    }

}
