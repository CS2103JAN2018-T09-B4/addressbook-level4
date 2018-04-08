package seedu.address.model.person.timetable;

//@@author marlenekoh

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class containing utility methods for comparing two timetables
 */
public class TimetableComparatorUtil {

    /**
     *
     * @param first
     * @param second
     */
    public static void compareTimetable(Timetable first, String second) {
        compareTimetable(first, new Timetable(second));
    }

    /**
     *
     * @param first
     * @param second
     */
    public static void compareTimetable(Timetable first, Timetable second) {
        ArrayList<TimetableModuleSlot> allUnsortedModulesSlots = setUpUnsortedModuleSlots(first, second);
        HashMap<String, ArrayList<TimetableModuleSlot>> sortedModuleSlots =
                TimetableParserUtil.sortModuleSlotsByDay(allUnsortedModulesSlots);
        String timetableString = TimetableDisplayUtil.convertTimetableToString(sortedModuleSlots);
        TimetableDisplayUtil.setUpTimetableDisplayInfoFile(timetableString);
        TimetableDisplayUtil.setUpTimetablePageScriptFile();
    }

    /**
     *
     * @param first
     * @param second
     * @return
     */
    private static ArrayList<TimetableModuleSlot> setUpUnsortedModuleSlots(Timetable first, Timetable second) {
        ArrayList<TimetableModuleSlot> allUnsortedModulesSlots = new ArrayList<TimetableModuleSlot>();
        allUnsortedModulesSlots.addAll(first.getAllModulesSlots());
        allUnsortedModulesSlots.addAll(second.getAllModulesSlots());

        for (TimetableModuleSlot t : allUnsortedModulesSlots) {
            t.setComparing(true);
        }
        return allUnsortedModulesSlots;
    }
}
