package seedu.address.model.person.timetable;

import static seedu.address.model.person.timetable.TimetableParserUtilTest.VALID_SHORT_URL;

import org.junit.Test;

//@@author marlenekoh
public class TimetableDisplayUtilTest {
    private static final String EXPECTED_FILE_CONTENTS_PATH = "src/test/data/sandbox/expectedTimetableDisplayInfo";
    private Timetable timetable = new Timetable(VALID_SHORT_URL);

    @Test
    public void writeToTimetableFile_assertsSameFileContents() {
        //TODO: Write TimetableDisplayUtilTest
        TimetableDisplayUtil.setUpTimetableDisplayInfo(timetable);
    }
}
