package seedu.address.model.person.timetable;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.testutil.Assert;

//@@author marlenekoh
public class TimetableUtilTest {

    public static final String VALID_LONG_URL = "https://nusmods.com/timetable/sem-2/"
            + "share?CS2101=SEC:C01&CS2103T=TUT:C01&CS3230=LEC:1,TUT:4&"
            + "CS3241=LAB:3,LEC:1,TUT:3&CS3247=LAB:1,LEC:1&GES1021=LEC:SL1";
    public static final String VALID_SHORT_URL = "http://modsn.us/wNuIW";
    public static final String INVALID_SHORT_URL = "http://modsn.us/123";
    private static final int CURRENT_SEMESTER = 2;
    private static HashMap<String, TimetableModule> expectedListOfModules;

    @Before
    public void setUp() {
        expectedListOfModules = new HashMap<String, TimetableModule>();
        HashMap<String, String> tempLessonPair;

        tempLessonPair = new HashMap<String, String>();
        tempLessonPair.put("Sectional Teaching", "C01");
        expectedListOfModules.put("CS2101", new TimetableModule("CS2101",
                tempLessonPair));

        tempLessonPair = new HashMap<String, String>();
        tempLessonPair.put("Tutorial", "C01");
        expectedListOfModules.put("CS2103T", new TimetableModule("CS2103T",
                tempLessonPair));

        tempLessonPair = new HashMap<String, String>();
        tempLessonPair.put("Lecture", "1");
        tempLessonPair.put("Tutorial", "4");
        expectedListOfModules.put("CS3230", new TimetableModule("CS3230",
                tempLessonPair));

        tempLessonPair = new HashMap<String, String>();
        tempLessonPair.put("Laboratory", "3");
        tempLessonPair.put("Lecture", "1");
        tempLessonPair.put("Tutorial", "3");
        expectedListOfModules.put("CS3241", new TimetableModule("CS3241",
                tempLessonPair));

        tempLessonPair = new HashMap<String, String>();
        tempLessonPair.put("Laboratory", "1");
        tempLessonPair.put("Lecture", "1");
        expectedListOfModules.put("CS3247", new TimetableModule("CS3247",
                tempLessonPair));

        tempLessonPair = new HashMap<String, String>();
        tempLessonPair.put("Lecture", "SL1");
        expectedListOfModules.put("GES1021", new TimetableModule("GES1021",
                tempLessonPair));
    }

    @Test
    public void expandShortTimetableUrl_invalidShortUrl_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.setExpandedTimetableUrl(
                        new Timetable(("")))); // empty string
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.setExpandedTimetableUrl(
                        new Timetable(("www.google.com")))); // invalid host
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.setExpandedTimetableUrl(
                        new Timetable(("http://www.facebook.com")))); // invalid host
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.setExpandedTimetableUrl(
                        new Timetable(("http://www.modsn.us/")))); // invalid host
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.setExpandedTimetableUrl(
                        new Timetable(("http://www.modsn.us/q7cLP")))); // invalid host
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.setExpandedTimetableUrl(
                        new Timetable(("http://www.modsn.us/")))); // code-part needs at least 1 character
    }

    @Test
    public void expandShortTimetableUrl_validUrl() throws ParseException {
        Timetable timetable = new Timetable(VALID_SHORT_URL);
        TimetableUtil.setExpandedTimetableUrl(timetable);
        assertEquals(timetable.getExpandedUrl(), VALID_LONG_URL);
    }

    @Ignore
    @Test
    public void expandShortTimetableUrl_invalidUrl_throwsParseException() {
        //TODO: Fix this bug
        Assert.assertThrows(ParseException.class, () ->
                TimetableUtil.setExpandedTimetableUrl(new Timetable(INVALID_SHORT_URL)));
    }

    @Test
    public void setUpTimetableInfo() {
        Timetable actualTimetable = new Timetable(VALID_SHORT_URL);

        Assert.assertDoesNotThrow(() -> TimetableUtil.setExpandedTimetableUrl(actualTimetable));
        assertEquals(VALID_LONG_URL, actualTimetable.getExpandedUrl());
        assertEquals(expectedListOfModules, actualTimetable.getListOfModules());
        assertEquals(CURRENT_SEMESTER, actualTimetable.getCurrentSemester());
    }
}
