package seedu.address.model.person.timetable;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.testutil.Assert;

public class TimetableUtilTest {

    private final String validLongUrl = "https://nusmods.com/timetable/sem-2/"
            + "share?CS2101=SEC:C01&CS2103T=TUT:C01&CS3230=LEC:1,TUT:4&"
            + "CS3241=LAB:3,LEC:1,TUT:3&CS3247=LAB:1,LEC:1&GES1021=LEC:SL1";
    private final String validShortUrl = "http://modsn.us/wNuIW";
    private final String invalidShortUrl = "http://modsn.us/123";
    private HashMap<String, TimetableModule> expectedListOfModules;

    @Before
    public void setUp() {
        expectedListOfModules = new HashMap<String, TimetableModule>();
        ArrayList<LessonPair> tempLessonPair;

        tempLessonPair = new ArrayList<LessonPair>();
        tempLessonPair.add(new LessonPair("SEC", "C01"));
        expectedListOfModules.put("CS2101", new TimetableModule("CS2101",
                tempLessonPair));

        tempLessonPair = new ArrayList<LessonPair>();
        tempLessonPair.add(new LessonPair("TUT", "C01"));
        expectedListOfModules.put("CS2103T", new TimetableModule("CS2103T",
                tempLessonPair));

        tempLessonPair = new ArrayList<LessonPair>();
        tempLessonPair.add(new LessonPair("LEC", "1"));
        tempLessonPair.add(new LessonPair("TUT", "4"));
        expectedListOfModules.put("CS3230", new TimetableModule("CS3230",
                tempLessonPair));

        tempLessonPair = new ArrayList<LessonPair>();
        tempLessonPair.add(new LessonPair("LAB", "3"));
        tempLessonPair.add(new LessonPair("LEC", "1"));
        tempLessonPair.add(new LessonPair("TUT", "3"));
        expectedListOfModules.put("CS3241", new TimetableModule("CS3241",
                tempLessonPair));

        tempLessonPair = new ArrayList<LessonPair>();
        tempLessonPair.add(new LessonPair("LAB", "1"));
        tempLessonPair.add(new LessonPair("LEC", "1"));
        expectedListOfModules.put("CS3247", new TimetableModule("CS3247",
                tempLessonPair));

        tempLessonPair = new ArrayList<LessonPair>();
        tempLessonPair.add(new LessonPair("LEC", "SL1"));
        expectedListOfModules.put("GES1021", new TimetableModule("GES1021",
                tempLessonPair));
    }

    @Test
    public void expandShortTimetableUrl_invalidShortUrl_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.expandShortTimetableUrl("")); // empty string
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.expandShortTimetableUrl("www.google.com")); // invalid host
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.expandShortTimetableUrl("http://www.facebook.com")); // invalid host
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.expandShortTimetableUrl("http://www.modsn.us/")); // invalid host
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.expandShortTimetableUrl("http://www.modsn.us/q7cLP")); // invalid host
        Assert.assertThrows(IllegalArgumentException.class, () ->
                TimetableUtil.expandShortTimetableUrl("http://www.modsn.us/")); // code-part needs at least 1 character
    }

    @Test
    public void expandShortTimetableUrl_validUrl() throws ParseException {
        String actualResult = TimetableUtil.expandShortTimetableUrl(validShortUrl);
        assertEquals(actualResult, validLongUrl);
    }

    @Test
    public void expandShortTimetableUrl_invalidUrl_throwsParseException() {
        Assert.assertThrows(ParseException.class, () ->
                TimetableUtil.expandShortTimetableUrl(invalidShortUrl));
    }

    @Test
    public void splitLongTimetableUrl () {
        HashMap<String, TimetableModule> actualListOfModules = TimetableUtil.splitLongTimetableUrl(validLongUrl);
        assertEquals(expectedListOfModules, actualListOfModules);
    }

    @Test
    public void setUpTimetableInfo() {
        Timetable expectedTimetable = new Timetable(validShortUrl);
        expectedTimetable.setExpandedUrl(validLongUrl);
        expectedTimetable.setListOfModules(expectedListOfModules);

        Timetable actualTimetable = new Timetable(validShortUrl);
        TimetableUtil.setUpTimetableInfo(actualTimetable);

        assertEquals(expectedTimetable.getExpandedUrl(), actualTimetable.getExpandedUrl());
        assertEquals(expectedTimetable.getListOfModules(), actualTimetable.getListOfModules());
    }
}
