package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyJournal;

/**
 * Represents a storage for {@link seedu.address.model.Journal}.
 */
public interface JournalStorage {

    /**
     * Returns the file path of the data file.
     */
    String getJournalFilePath();

    /**
     * Returns AddressBook data as a {@link ReadOnlyJournal}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyJournal> readJournal() throws DataConversionException, IOException;

    /**
     * @see #getJournalFilePath()
     */
    Optional<ReadOnlyJournal> readJournal(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyJournal} to the storage.
     * @param journal cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveJournal(ReadOnlyJournal journal) throws IOException;

    /**
     * @see #saveJournal(ReadOnlyJournal)
     */
    void saveJournal(ReadOnlyJournal journal, String filePath) throws IOException;

}
