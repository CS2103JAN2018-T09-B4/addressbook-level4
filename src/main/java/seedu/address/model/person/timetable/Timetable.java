package seedu.address.model.person.timetable;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;
import java.util.HashMap;

//@@author marlenekoh
/**
 * Represents the NUSMODS timetable of the partner
 */
public class Timetable {
    public static final String MESSAGE_TIMETABLE_CONSTRAINTS = "Short NUSMods timetable URL should be of the format "
            + "http://modsn.us/code-part "
            + "and adhere to the following constraints:\n"
            + "1. The URL should start with http://modsn.us/\n"
            + "2. The code-part should only contain alphanumeric characters.";
    private static final String SHORT_NUSMODS_URL_REGEX = "http://modsn.us/";
    private static final String CODE_PART_REGEX = "[\\w]+";
    private static final String TIMETABLE_VALIDATION_REGEX = SHORT_NUSMODS_URL_REGEX + CODE_PART_REGEX;
    private static int currentSemester;
    private static HashMap<String, ArrayList<TimetableModuleSlot>>
            listOfDays; // HashMap of <Day, Sorted list of TimetableModuleSlots>
    private static HashMap<String, TimetableModule> listOfModules; // HashMap of <module code, TimetableModule>
    private static String expandedUrl;

    public final String value;

    public Timetable(String timetableUrl) {
        requireNonNull(timetableUrl);
        checkArgument(isValidTimetable(timetableUrl), MESSAGE_TIMETABLE_CONSTRAINTS);
        this.value = timetableUrl;
        TimetableParserUtil.setUpTimetableInfo(this);
        TimetableDisplayUtil.setUpTimetableDisplayInfo(this);
    }

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }

    public void setListOfModules(HashMap<String, TimetableModule> listOfModules) {
        this.listOfModules = listOfModules;
    }

    public HashMap<String, TimetableModule> getListOfModules() {
        return listOfModules;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public void setListOfDays(HashMap<String, ArrayList<TimetableModuleSlot>> listOfDays) {
        this.listOfDays = listOfDays;
    }

    public static HashMap<String, ArrayList<TimetableModuleSlot>> getListOfDays() {
        return listOfDays;
    }

    /**
     * Returns if a given string is a valid short NUSMods timetable URL.
     */
    public static boolean isValidTimetable(String test) {
        return test.matches(TIMETABLE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Timetable // instanceof handles nulls
                && this.value.equals(((Timetable) other).value)); // state check
    }
}
