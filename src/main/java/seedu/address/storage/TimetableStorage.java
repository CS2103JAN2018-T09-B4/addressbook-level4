package seedu.address.storage;

import java.io.FileNotFoundException;

//@@author marlenekoh
/**
 * Represents a storage for {@link seedu.address.model.person.timetable}.
 */
public interface TimetableStorage {

    /**
     * Updates TimetablePageScript file at path {@code timetablePageJsPath} with new timetable module information
     */
    void setUpTimetablePageScriptFile();

    /**
     * Writes the given string to {@code timetableInfoFilePath}
     * @param toWrite contents to write to {@code timetableInfoFilePath}
     */
    void setUpTimetableDisplayFiles(String toWrite);

    /**
     * Writes a string to the file at {@code path}
     * @param toWrite the String to write
     * @param path the path of the file
     */
    void writeToFile(String toWrite, String path);

    /**
     * Gets file contents from the file at the given path
     * @return String containing file contents
     */
    String getFileContents(String path) throws FileNotFoundException;

    /**
     * Replaces first line of {@code contents} with {@code replace}
     * @param contents original content of the javascript file
     * @param replace new line
     * @return new content
     */
    String replaceFirstLine(String contents, String replace);
}
