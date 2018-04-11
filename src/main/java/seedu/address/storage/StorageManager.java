package seedu.address.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.JournalChangedEvent;
import seedu.address.commons.events.model.TimetableChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.events.ui.ShowTimetableRequestEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyJournal;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private AddressBookStorage addressBookStorage;
    private JournalStorage journalStorage;
    private TimetableStorage timetableStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(AddressBookStorage addressBookStorage, JournalStorage journalStorage,
                          UserPrefsStorage userPrefsStorage, TimetableStorage timetableStorage) {
        super();
        this.addressBookStorage = addressBookStorage;
        this.journalStorage = journalStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.timetableStorage = timetableStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public String getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public String getAddressBookFilePath() {
        return addressBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException {
        return readAddressBook(addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return addressBookStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        addressBookStorage.saveAddressBook(addressBook, filePath);
    }


    @Override
    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveAddressBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    // ================ Journal methods ==============================

    //@@author traceurgan
    @Override
    public String getJournalFilePath() {
        return journalStorage.getJournalFilePath();
    }

    @Override
    public Optional<ReadOnlyJournal> readJournal() throws DataConversionException, IOException {
        return readJournal(journalStorage.getJournalFilePath());
    }

    @Override
    public Optional<ReadOnlyJournal> readJournal(String filePath) throws DataConversionException, IOException {
        return journalStorage.readJournal(filePath);
    }

    @Override
    public void saveJournal(ReadOnlyJournal journal) throws IOException {
        saveJournal(journal, journalStorage.getJournalFilePath());
        logger.info(getJournalFilePath());
    }

    @Override
    public void saveJournal(ReadOnlyJournal journal, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        journalStorage.saveJournal(journal, filePath);
    }

    /**
     * Saves the current version of the Journal Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    @Subscribe
    public void handleJournalChangedEvent(JournalChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveJournal(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    // ================ Timetable methods ==============================

    //@@author marlenekoh
    @Override
    public void setUpTimetablePageScriptFile() {
        timetableStorage.setUpTimetablePageScriptFile();
    }

    @Override
    public void setUpTimetableDisplayFiles(String toWrite) {
        timetableStorage.setUpTimetableDisplayFiles(toWrite);
    }

    @Override
    public void writeToFile(String toWrite, String path) {
        timetableStorage.writeToFile(toWrite, path);
    }

    @Override
    public String getFileContents(String path) throws FileNotFoundException {
        return timetableStorage.getFileContents(path);
    }

    @Override
    public String replaceFirstLine(String contents, String replace) {
        return timetableStorage.replaceFirstLine(contents, replace);
    }

    @Subscribe
    public void handleTimetableChangedEvent(TimetableChangedEvent event) {
        setUpTimetableDisplayFiles(event.timetable.getTimetableDisplayInfo());
        setUpTimetablePageScriptFile();
        EventsCenter.getInstance().post(new ShowTimetableRequestEvent());
    }
}
