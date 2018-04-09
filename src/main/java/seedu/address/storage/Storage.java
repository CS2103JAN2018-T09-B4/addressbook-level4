package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.events.model.JournalChangedEvent;
import seedu.address.commons.events.model.PersonChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * API of the Storage component
 */
public interface Storage extends PersonStorage, UserPrefsStorage, JournalStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */

    //@@author traceurgan

    String getPersonFilePath();

    Optional<ReadOnlyPerson> readPerson() throws DataConversionException, IOException;

    Optional<ReadOnlyPerson> readPerson(String filePath) throws DataConversionException, IOException;

    void savePerson(ReadOnlyPerson person) throws IOException;

    void savePerson(ReadOnlyPerson person, String filePath) throws IOException;

    /**
     * Saves the current Person to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    @Subscribe
    void handlePersonChangedEvent(PersonChangedEvent event);

    /**
     * Saves the current version of the Journal to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleJournalChangedEvent(JournalChangedEvent jce);
}
