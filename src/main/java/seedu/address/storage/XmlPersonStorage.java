package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * A class to access AddressBook data stored as an xml file on the hard disk.
 */
public class XmlPersonStorage implements PersonStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlPersonStorage.class);

    private String filePath;

    public XmlPersonStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getPersonFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyPerson> readPerson() throws DataConversionException, IOException {
        return readPerson(filePath);
    }

    /**
     * Similar to {@link #readPerson()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyPerson> readPerson(String filePath) throws DataConversionException,
                                                                                 IOException {
        requireNonNull(filePath);

        File personFile = new File(filePath);

        if (!personFile.exists()) {
            logger.info("AddressBook file "  + personFile + " not found");
            return Optional.empty();
        }

        XmlSerializablePerson xmlPerson = XmlFileStorage.loadDataFromSaveFile(new File(filePath));
        if (xmlPerson.getSize() == 0) {
            return Optional.empty();
        }
        try {
            return Optional.of(xmlPerson.toModelType());
        } catch (IllegalValueException | ParseException ive) {
            logger.info("Illegal values found in " + personFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void savePerson(ReadOnlyPerson person) throws IOException {
        savePerson(person, filePath);
    }

    /**
     * Similar to {@link #savePerson(ReadOnlyPerson)} (ReadOnlyPerson)}
     * @param filePath location of the data. Cannot be null
     */
    public void savePerson(ReadOnlyPerson person, String filePath) throws IOException {
        XmlSerializablePerson xsp;
        if (person == null) {
            xsp = new XmlSerializablePerson();
        } else {
            requireNonNull(person);
            xsp = new XmlSerializablePerson(person);
        }
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, xsp);

    }

}
