package seedu.address.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.util.SampleDataUtil;

//@@author marlenekoh
/**
 * A class to write and read Timetable data stored on the hard disk.
 */
public class FileTimetableStorage implements TimetableStorage {
    private static final Logger logger = LogsCenter.getLogger(FileTimetableStorage.class);

    private String timetablePageJsPath;
    private String timetableInfoFilePath;

    public FileTimetableStorage(String timetablePageJsPath, String timetableInfoFilePath) {
        this.timetablePageJsPath = timetablePageJsPath;
        this.timetableInfoFilePath = timetableInfoFilePath;
    }

    public String getTimetablePageJsPath() {
        return timetablePageJsPath;
    }

    public String getTimetableInfoFilePath() {
        return timetableInfoFilePath;
    }

    @Override
    public void setUpTimetableDisplayFiles(String toWrite) {
        writeToFile(toWrite, timetableInfoFilePath);
        setUpTimetablePageScriptFile();
    }

    @Override
    public void setUpTimetablePageScriptFile() {
        try {
            writeToFile(SampleDataUtil.getDefaultTimetablePageScript(), timetablePageJsPath);
            String oldContent = getFileContents(timetablePageJsPath);
            String toReplace = getFileContents(timetableInfoFilePath);
            String newContent = replaceFirstLine(oldContent, toReplace);
            writeToFile(newContent, timetablePageJsPath);
        } catch (FileNotFoundException e) {
            logger.warning("File not found");
        }
    }

    @Override
    public void writeToFile(String toWrite, String path) {
        File file = new File(path);
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(toWrite);
            printWriter.close();
        } catch (FileNotFoundException e) {
            logger.warning("File not found");
        }
    }

    @Override
    public String getFileContents(String path) throws FileNotFoundException {
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
                throw new FileNotFoundException("File does not exist");
            }
        } catch (IOException e) {
            logger.warning("Exception in reading file");
        }
        return null;
    }

    @Override
    public String replaceFirstLine(String contents, String replace) {
        StringBuilder sb = new StringBuilder();
        //TODO: Get rid of this
        sb.append("//@@author marlenekoh\n");
        sb.append("timetable = [");
        sb.append(replace);
        int pos = contents.indexOf(';');
        sb.append(contents.substring(pos - 1));
        return sb.toString();
    }
}
