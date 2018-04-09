package seedu.address.storage;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.XmlUtil;

/**
 * Stores addressbook data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given addressbook data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializablePerson addressBook)
            throws IOException {
        try {
            XmlUtil.saveDataToFile(file, addressBook);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }

    /**
     * Returns address book in the file or an empty address book
     */
    public static XmlSerializablePerson loadDataFromSaveFile(File file) throws DataConversionException,
                                                                            IOException {
        try {
            XmlSerializablePerson xmlBook =  XmlUtil.getDataFromFile(file, XmlSerializablePerson.class);
            return xmlBook;
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

    /**
     * Returns address book in the file or an empty address book
     */
    public static XmlSerializableJournal loadJournalFromSaveFile(File file) throws DataConversionException,
            IOException {
        try {
            XmlSerializableJournal xmlJournal =  XmlUtil.getDataFromFile(file, XmlSerializableJournal.class);
            return xmlJournal;
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

    /**
     * Saves the given journal data to the specified file.
     */
    public static void saveJournalToFile(File file, XmlSerializableJournal journal)
            throws IOException {
        try {
            XmlUtil.saveDataToFile(file, journal);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }
}
