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
    public static void saveDataToFile(File file, XmlSerializableAddressBook addressBook)
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
    public static XmlSerializableAddressBook loadDataFromSaveFile(File file) throws DataConversionException,
                                                                            IOException {
        try {
            XmlSerializableAddressBook xmlBook =  XmlUtil.getDataFromFile(file, XmlSerializableAddressBook.class);
            return xmlBook;
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
