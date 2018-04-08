package seedu.address.model.person.timetable;

import static seedu.address.model.person.timetable.TimetableParserUtil.FRIDAY_INDEX;
import static seedu.address.model.person.timetable.TimetableParserUtil.MONDAY_INDEX;
import static seedu.address.model.person.timetable.TimetableParserUtil.THURSDAY_INDEX;
import static seedu.address.model.person.timetable.TimetableParserUtil.TUESDAY_INDEX;
import static seedu.address.model.person.timetable.TimetableParserUtil.WEDNESDAY_INDEX;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;

//@@author marlenekoh
/**
 * A class containing utility methods for displaying a Timetable in the browser panel.
 */
public class TimetableDisplayUtil {
    public static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    public static final String[] TIMES = {
        "0800", "0830", "0900", "0930", "1000", "1030", "1100", "1130",
        "1200", "1230", "1300", "1330", "1400", "1430", "1500", "1530",
        "1600", "1630", "1700", "1730", "1800", "1830", "1900", "1930",
        "2000", "2030", "2100", "2130", "2200", "2230", "2300", "2330"
    };
    public static final String[] WEEKS = {"Odd Week", "Even Week", "Every Week"};
    private static String timetableInfoFilePath = "src/main/resources/timetableDisplayInfo";
    private static String timetablePageJsPath = "src/main/resources/view/TimetablePageScript.js";
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    private static final String DEFAULT_TIMETABLE_PAGE_SCRIPT = "//@@author marlenekoh\n"
            + "timetable = [\"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"CS2103T\","
            + " \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"\n"
            + "];\n"
            + "var myTimetable = \"\";\n"
            + "var nRows = \"\";\n"
            + "var nCells = \"\";\n"
            + "function displaySchedule(){\n"
            + "    for (i=0; i<nRows; i++) {\n"
            + "        for (n=0; n<nCells; n++) {\n"
            + "            myTimetable.rows[i+1].cells[n+1].lastChild.data = timetable[n+(i*nCells)];\n"
            + "        }\n"
            + "    }\n"
            + "}\n"
            + "function mapTable(){\n"
            + "    myTimetable = document.getElementById('myTimetable');\n"
            + "    nRows = myTimetable.rows.length-1;\n"
            + "    nCells = myTimetable.rows[0].cells.length-1;\n"
            + "    displaySchedule();\n"
            + "}\n"
            + "onload=mapTable;\n";

    /**
     * Sets up the javascript file at path {@code timetablePageJsPath}
     * @param timetable Timetable to be set up
     */
    public static void setUpTimetableDisplayInfo(Timetable timetable) {
        setUpTimetableDisplayInfoFile(timetable);
        setUpTimetablePageScriptFile();
    }

    /**
     * Updates TimetablePageScript file at path {@code timetablePageJsPath} with new timetable module information
     */
    public static void setUpTimetablePageScriptFile() {
        String oldContent = getFileContents(timetablePageJsPath);
        String toReplace = getFileContents(timetableInfoFilePath);
        String newContent = replaceFirstLine(oldContent, toReplace);
        writeToTimetablePageScriptFile(newContent);
    }

    /**
     * Writes Timetable information to a text file
     * @param timetable the timetable to convert into string and write
     */
    public static void setUpTimetableDisplayInfoFile(Timetable timetable) {
        File timetableDisplayInfo = new File(timetableInfoFilePath);
        try {
            PrintWriter printWriter = new PrintWriter(timetableDisplayInfo);
            String toWrite = convertTimetableToString(timetable);
            printWriter.write(toWrite);
            timetable.setTimetableDisplayInfo(toWrite);
            printWriter.close();
        } catch (FileNotFoundException e) {
            logger.warning("File not found, creating new file");
            try {
                timetableInfoFilePath = "data/timetableDisplayInfo";
                timetableDisplayInfo = new File(timetableInfoFilePath);
                timetableDisplayInfo.createNewFile();
                setUpTimetableDisplayInfo(timetable);
            } catch (IOException ioe) {
                logger.severe("Unable to create new file");
            }
        }
    }

    /**
     * Writes a string to the file at {@code timetableInfoFilePath}
     * @param toWrite the String to write
     */
    public static void setUpTimetableDisplayInfoFile(String toWrite) {
        File timetableDisplayInfo = new File(timetableInfoFilePath);
        try {
            PrintWriter printWriter = new PrintWriter(timetableDisplayInfo);
            printWriter.write(toWrite);
            printWriter.close();
        } catch (FileNotFoundException e) {
            logger.warning("File not found, creating new file");
            try {
                timetableInfoFilePath = "data/timetableDisplayInfo";
                timetableDisplayInfo = new File(timetableInfoFilePath);
                timetableDisplayInfo.createNewFile();
                setUpTimetableDisplayInfoFile(toWrite);
            } catch (IOException ioe) {
                logger.severe("Unable to create new file");
            }
        }
    }

    /**
     * Converts the {@code listOfDays} belonging to {@code timetable} into a String object for parsing
     * @param timetable which contains schedule to convert into JSON object
     */
    public static String convertTimetableToString(Timetable timetable) {
        return convertTimetableToString(timetable.getListOfDays());
    }

    /**
     * Converts the {@code listOfDays} into a String object for parsing
     * @param listOfDays ArrayLists of timetableModuleSlots sorted by time
     */
    public static String convertTimetableToString(HashMap<String, ArrayList<TimetableModuleSlot>> listOfDays) {
        StringBuilder sb = new StringBuilder();

        ArrayList<TimetableModuleSlot> slotsForTheDay = null;
        for (int i = 0; i < TIMES.length; i++) {
            if (i < TIMES.length - 1) {
                sb.append(listOfDays.get(DAYS[MONDAY_INDEX].toUpperCase()).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[TUESDAY_INDEX].toUpperCase()).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[WEDNESDAY_INDEX].toUpperCase()).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[THURSDAY_INDEX].toUpperCase()).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[FRIDAY_INDEX].toUpperCase()).get(i).toString());
                sb.append(", ");
            } else {
                sb.append(listOfDays.get(DAYS[MONDAY_INDEX].toUpperCase()).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[TUESDAY_INDEX].toUpperCase()).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[WEDNESDAY_INDEX].toUpperCase()).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[THURSDAY_INDEX].toUpperCase()).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[FRIDAY_INDEX].toUpperCase()).get(i).toString());
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Gets file contents from the file at the given path
     * @return String containing file contents
     */
    public static String getFileContents(String path) {
        File file = new File(path);
        try {
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = br.readLine();
                }
                br.close();
                return sb.toString();
            } else {
                timetablePageJsPath = "data/TimetablePageScript.js";
                writeToTimetablePageScriptFile(DEFAULT_TIMETABLE_PAGE_SCRIPT);
                return DEFAULT_TIMETABLE_PAGE_SCRIPT;
            }
        } catch (IOException e) {
            logger.warning("Exception in reading file");
        }
        return null;
    }

    /**
     * Replaces file contents of file at {@code timetablePageJsPath} with {@code contents}
     * @param contents the new contents of the file
     */
    public static void writeToTimetablePageScriptFile(String contents) {
        File timetablePageScript = new File(timetablePageJsPath);
        try {
            PrintWriter printWriter = new PrintWriter(timetablePageScript);
            printWriter.write(contents);
            printWriter.close();
        } catch (FileNotFoundException e) {
            logger.warning("File not found");
        }
    }

    /**
     * Replaces first line of {@code contents} with {@code replace}
     * @param contents original content of the javascript file
     * @param replace new line
     * @return new content
     */
    public static String replaceFirstLine(String contents, String replace) {
        StringBuilder sb = new StringBuilder();
        sb.append("//@@author marlenekoh\n");
        sb.append("timetable = [");
        sb.append(replace);
        int pos = contents.indexOf(';');
        sb.append(contents.substring(pos - 1));
        return sb.toString();
    }
}
