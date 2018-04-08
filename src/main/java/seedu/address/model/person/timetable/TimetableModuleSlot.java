package seedu.address.model.person.timetable;

//@@author marlenekoh
/**
 * Represents the module information of one module slot in one day
 */
public class TimetableModuleSlot implements Comparable<TimetableModuleSlot> {
    private String moduleCode;
    private String lessonType;
    private String classType;
    private String weekFreq;
    private String day;
    private String venue;
    private String startTime;
    private String endTime;
    private boolean isComparing; // for comparing timetables
    private final boolean isEmpty; // for displaying normal timetable

    public TimetableModuleSlot() {
        this.moduleCode = null;
        this.lessonType = null;
        this.classType = null;
        this.weekFreq = null;
        this.day = null;
        this.venue = null;
        this.startTime = null;
        this.endTime = null;
        isEmpty = true;
        isComparing = false;
    }

    public TimetableModuleSlot(String moduleCode, String lessonType, String classType, String weekFreq, String day,
                               String venue, String startTime, String endTime) {
        this.moduleCode = moduleCode;
        this.lessonType = lessonType;
        this.classType = classType;
        this.weekFreq = weekFreq;
        this.day = day;
        this.venue = venue;
        this.startTime = startTime;
        this.endTime = endTime;
        isEmpty = false;
        isComparing = false;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getLessonType() {
        return lessonType;
    }

    public String getClassType() {
        return classType;
    }

    public String getWeekFreq() {
        return weekFreq;
    }

    public String getDay() {
        return day;
    }

    public String getVenue() {
        return venue;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setComparing(boolean comparing) {
        isComparing = comparing;
    }

    @Override
    public int compareTo(TimetableModuleSlot other) {
        return this.startTime.compareTo(other.startTime);
    }

    @Override
    public String toString() {
        if (isComparing) {
            return isEmpty
                    ? "\"\"" : "\"X\"";
        } else {
            return isEmpty
                    ? "\"\"" : "\"" + moduleCode + "\"";
        }
    }
}
