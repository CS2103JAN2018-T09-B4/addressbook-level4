package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Represents a storage for {@link Person}.
 */
public interface PersonStorage {

    /**
     * Returns the file path of the data file.
     */
    String getPersonFilePath();

    /**
     * Returns Person data as a {@link ReadOnlyPerson}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyPerson> readPerson() throws DataConversionException, IOException;

    /**
     * @see #getPersonFilePath() () FilePath()
     */
    Optional<ReadOnlyPerson> readPerson(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyPerson} to the storage.
     * @param person cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void savePerson(ReadOnlyPerson person) throws IOException;

    /**
     * @see #savePerson(ReadOnlyPerson)
     */
    void savePerson(ReadOnlyPerson person, String filePath) throws IOException;

}
