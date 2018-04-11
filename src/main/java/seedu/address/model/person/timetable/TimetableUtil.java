package seedu.address.model.person.timetable;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author marlenekoh
/**
 * A class containing utility methods for parsing an NUSMods short URL and setting up a Timetable
 */
public class TimetableUtil {
    public static final int MONDAY_INDEX = 0;
    public static final int TUESDAY_INDEX = 1;
    public static final int WEDNESDAY_INDEX = 2;
    public static final int THURSDAY_INDEX = 3;
    public static final int FRIDAY_INDEX = 4;
    public static final String[] DAYS = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};
    public static final String[] TIMES = {
        "0800", "0830", "0900", "0930", "1000", "1030", "1100", "1130",
        "1200", "1230", "1300", "1330", "1400", "1430", "1500", "1530",
        "1600", "1630", "1700", "1730", "1800", "1830", "1900", "1930",
        "2000", "2030", "2100", "2130", "2200", "2230", "2300", "2330"
    };
    public static final String[] WEEKS = {"Odd Week", "Even Week", "Every Week"};
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    private static final String SPLIT_QUESTION_MARK = "\\?";
    private static final String SPLIT_AMPERSAND = "&";
    private static final String SPLIT_EQUALS = "=";
    private static final String SPLIT_COMMA = ",";
    private static final String SPLIT_COLON = ":";
    private static final String REPLACE_NON_DIGIT_CHARACTERS = "[^0-9]";
    private static final String INVALID_SHORT_URL_RESULT = "http://modsn.us";
    private static final String MESSAGE_INVALID_SHORT_URL = "Invalid short NUSMods URL provided.";
    private static final String MESSAGE_INVALID_CLASS_TYPE = "Invalid class type";
    private static final String MESSAGE_INVALID_DAY_TYPE = "Invalid day type";
    private static final String HTTP_METHOD_GET = "GET";
    private static final int HTTP_METHOD_RESPONSE_OK = 200;
    private static final int SEM_NUMBER_INDEX = 0;
    private static final int MODULE_INFORMATION_INDEX = 1;
    private static final int MODULE_CODE_INDEX = 0;
    private static final int MODULE_CODE_REMAINING_INDEX = 1;
    private static final int LESSON_TYPE_INDEX = 0;
    private static final int CLASS_TYPE_INDEX = 1;

    /**
     * Sets up attributes of a given {@code Timetable}.
     * @param timetable Timetable to be set up
     */
    public static void setUpTimetableInfo(Timetable timetable) {
        try {
            setExpandedTimetableUrl(timetable);
            setSemNumFromExpandedUrl(timetable);
            setListOfModules(timetable);
            setListOfDays(timetable);
            setTimetableDisplayInfo(timetable);
        } catch (ParseException e) {
            logger.warning(MESSAGE_INVALID_SHORT_URL);
        }
    }

    /**
     * Sets the expanded URL for {@code timetable}.
     * @param timetable Timetable whose expanded URL is to be set
     */
    public static void setExpandedTimetableUrl(Timetable timetable) throws ParseException {
        String expandedUrl = expandShortTimetableUrl(timetable);
        timetable.setExpandedUrl(expandedUrl);
    }

    /**
     * Expands short NUSMods timetable URL to a long NUSMods timetable URL from {@timetable}.
     * @param timetable whose url is to be parsed
     * @return expanded NUSMods timetable URL
     * @throws ParseException if short url provided is invalid short NUSMods timetable URL
     */
    private static String expandShortTimetableUrl(Timetable timetable) throws ParseException {
        String timetableUrl = timetable.value;
        checkArgument(Timetable.isValidTimetable(timetableUrl), Timetable.MESSAGE_TIMETABLE_CONSTRAINTS);
        String expandedUrl = null;
        try {
            final URL shortUrl = new URL(timetableUrl);
            final HttpURLConnection urlConnection = (HttpURLConnection) shortUrl.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            expandedUrl = urlConnection.getHeaderField("location");

            if (expandedUrl.equals(INVALID_SHORT_URL_RESULT)) {
                throw new ParseException(MESSAGE_INVALID_SHORT_URL);
            }
        } catch (MalformedURLException e) {
            logger.warning("URL provided is malformed");
        } catch (IOException e) {
            logger.warning("Failed to open connection");
        }
        return expandedUrl;
    }

    /**
     * Sets the {@code currentSemester} for {@code timetable}.
     * @param timetable Timetable whose {@code currentSemester} is to be set
     */
    private static void setSemNumFromExpandedUrl(Timetable timetable) {
        timetable.setCurrentSemester(getSemNumFromExpandedUrl(timetable));
    }

    /**
     * Parses for {@code currentSemester} from expandedUrl of {@code timetable}
     * @param timetable whose {@code currentSemester} is to be set
     */
    private static int getSemNumFromExpandedUrl(Timetable timetable) {
        String expandedUrl = timetable.getExpandedUrl();
        requireNonNull(expandedUrl);
        String[] moduleInformation = expandedUrl.split(SPLIT_QUESTION_MARK);
        return Integer.valueOf(moduleInformation[SEM_NUMBER_INDEX]
                .replaceAll(REPLACE_NON_DIGIT_CHARACTERS, ""));
    }

    /**
     * Sets listOfModules in {@code timetable}
     * @param timetable whose long url is to be split
     */
    public static void setListOfModules(Timetable timetable) {
        HashMap<String, TimetableModule> listOfModules = splitExpandedUrl(timetable);
        timetable.setListOfModules(listOfModules);
    }

    /**
     * Splits expanded NUSMods timetable URL into the different {@code TimetableModule}s.
     * Expanded timetable URL is in the format ...?[MODULE_CODE]=[LESSON_TYPE]:[CLASS_NUM]&...
     * @param timetable whose long url is to be split
     * @return HashMap containing list of modules
     */
    private static HashMap<String, TimetableModule> splitExpandedUrl(Timetable timetable) {
        String expandedUrl = timetable.getExpandedUrl();
        requireNonNull(expandedUrl);
        String[] moduleInformation = expandedUrl.split(SPLIT_QUESTION_MARK);
        String[] modules = moduleInformation[MODULE_INFORMATION_INDEX].split(SPLIT_AMPERSAND);

        HashMap<String, TimetableModule> listOfModules = new  HashMap<String, TimetableModule>();
        HashMap<String, String> listOfLessons;
        String moduleCode;
        String lessonType;
        String classType;
        String[] lessons;

        for (String currentModule : modules) {
            listOfLessons = new HashMap<String, String>();

            moduleCode = currentModule.split(SPLIT_EQUALS)[MODULE_CODE_INDEX];
            lessons = currentModule.split(SPLIT_EQUALS)[MODULE_CODE_REMAINING_INDEX].split(SPLIT_COMMA);
            for (String currLesson : lessons) {
                lessonType = currLesson.split(SPLIT_COLON)[LESSON_TYPE_INDEX];
                classType = currLesson.split(SPLIT_COLON)[CLASS_TYPE_INDEX];

                try {
                    listOfLessons.put(convertLessonType(lessonType), classType);
                } catch (IllegalValueException e) {
                    logger.warning("Unable to convert lesson type");
                }
            }
            listOfModules.put(moduleCode, new TimetableModule(moduleCode, listOfLessons));
        }
        return listOfModules;
    }

    /**
     * Sets {@code listOfDays} in {@code timetable} given using the NUSMods short url
     * @param timetable timetable to set List of days
     */
    public static void setListOfDays(Timetable timetable) {
        requireNonNull(timetable.getListOfModules());
        ArrayList<TimetableModuleSlot> allTimetableModuleSlots = retrieveModuleSlotsFromApi(timetable);
        timetable.setAllModulesSlots(allTimetableModuleSlots);
        HashMap<String, ArrayList<TimetableModuleSlot>> sortedTimetableModuleSlots =
                sortModuleSlotsByDay(allTimetableModuleSlots);
        timetable.setListOfDays(sortedTimetableModuleSlots);
    }

    public static void setTimetableDisplayInfo(Timetable timetable) {
        timetable.setTimetableDisplayInfo(convertListOfDaysToString(timetable.getListOfDays()));
    }

    /**
     * Converts the {@code listOfDays} into a String object for parsing.
     */
    private static String convertListOfDaysToString(HashMap<String, ArrayList<TimetableModuleSlot>> listOfDays) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < TIMES.length; i++) {
            if (i < TIMES.length - 1) {
                sb.append(listOfDays.get(DAYS[MONDAY_INDEX]).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[TUESDAY_INDEX]).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[WEDNESDAY_INDEX]).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[THURSDAY_INDEX]).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[FRIDAY_INDEX]).get(i).toString());
                sb.append(", ");
            } else {
                sb.append(listOfDays.get(DAYS[MONDAY_INDEX]).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[TUESDAY_INDEX]).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[WEDNESDAY_INDEX]).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[THURSDAY_INDEX]).get(i).toString());
                sb.append(", ");
                sb.append(listOfDays.get(DAYS[FRIDAY_INDEX]).get(i).toString());
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Gets module information from nusmods api for the all modules in listOfModules in {@code timetable}
     * @param timetable containing list of all modules
     */
    private static ArrayList<TimetableModuleSlot> retrieveModuleSlotsFromApi(Timetable timetable) {
        String currentModuleInfo;
        ArrayList<TimetableModuleSlot> allTimetableModuleSlots = new ArrayList<TimetableModuleSlot>();
        Set<Map.Entry<String, TimetableModule>> entrySet = timetable.getListOfModules().entrySet();

        for (Map.Entry<String, TimetableModule> currentModule : entrySet) {
            //TODO: Remove magic number acadYear
            currentModuleInfo = getJsonContentsFromNusModsApiForModule("2017-2018",
                    Integer.toString(timetable.getCurrentSemester()), currentModule.getKey());
            allTimetableModuleSlots.addAll(getAllTimetableModuleSlots(currentModuleInfo, timetable,
                    currentModule.getKey()));
        }
        return allTimetableModuleSlots;
    }

    /**
     * Retrieves json file from NUSMods api and converts to String
     * @param acadYear String representing academic year
     * @param semNum String representing semester number
     * @param moduleCode String representing module code
     * Format: http://api.nusmods.com/[acadYear]/[semNum]/modules/[moduleCode].json
     * e.g. http://api.nusmods.com/2017-2018/2/modules/CS3241.json
     * @return String containing contents of json file
     */
    private static String getJsonContentsFromNusModsApiForModule(String acadYear, String semNum, String moduleCode) {
        String contents = null;
        String nusmodsApiUrlString = "http://api.nusmods.com/" + acadYear + "/" + semNum + "/modules/" + moduleCode
                + ".json";
        try {
            URL nusmodsApiUrl = new URL(nusmodsApiUrlString);
            HttpURLConnection urlConnection = (HttpURLConnection) nusmodsApiUrl.openConnection();
            urlConnection.setRequestMethod(HTTP_METHOD_GET);
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HTTP_METHOD_RESPONSE_OK) {
                contents = readStream(urlConnection.getInputStream());
            } else {
                contents = "Error in accessing API - " + readStream(urlConnection.getErrorStream());
            }
        } catch (MalformedURLException e) {
            logger.warning("URL provided is malformed");
        } catch (ProtocolException e) {
            logger.warning("Protocol exception");
        } catch (IOException e) {
            logger.warning("Failed to open connection");
        }
        return contents;
    }

    /**
     * Read the responded result and stores in a String
     * @param inputStream from nusmods api
     * @return String containing contents of nusmods api
     * @throws IOException from readLine()
     */
    private static String readStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;

        while ((inputLine = reader.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        reader.close();
        return stringBuilder.toString();
    }

    /**
     * Parses contents of json file contents result from {@code readStream()}
     * @param contents contents of json file in String
     * @param timetable timetable to set list of modules slots
     * @param moduleCode current module
     * @return all TimetableModuleSlots for the timetable
     */
    public static ArrayList<TimetableModuleSlot> getAllTimetableModuleSlots(String contents, Timetable timetable,
                                                               String moduleCode) {
        requireNonNull(timetable.getListOfModules());

        JSONObject jsonObject = null;
        JSONParser parser = new JSONParser();
        ArrayList<TimetableModuleSlot> listOfModuleSlots = new ArrayList<TimetableModuleSlot>();

        try {
            Object obj = parser.parse(contents);
            jsonObject = (JSONObject) obj;
            JSONArray arrOfClassInformation = null;
            Object object = jsonObject.get("Timetable");
            arrOfClassInformation = (JSONArray) object;

            String tempLessonType;
            String tempClassType;
            String tempWeekFreq;
            String tempDay;
            String tempStartTime;
            String tempEndTime;
            String tempVenue;

            HashMap<String, TimetableModule> listOfModules = timetable.getListOfModules();
            TimetableModule timetableModule = listOfModules.get(moduleCode);
            HashMap<String, String> listOfLessons = timetableModule.getListOfLessons();

            for (Object t : arrOfClassInformation) {
                tempLessonType = ((JSONObject) t).get("LessonType").toString();
                tempClassType = ((JSONObject) t).get("ClassNo").toString();

                if (listOfLessons.get(tempLessonType).equals(tempClassType)) {
                    tempWeekFreq = ((JSONObject) t).get("WeekText").toString();
                    tempDay = ((JSONObject) t).get("DayText").toString();
                    tempStartTime = ((JSONObject) t).get("StartTime").toString();
                    tempEndTime = ((JSONObject) t).get("EndTime").toString();
                    tempVenue = ((JSONObject) t).get("Venue").toString();
                    listOfModuleSlots.add(new TimetableModuleSlot(moduleCode, tempLessonType, tempClassType,
                            tempWeekFreq, tempDay, tempVenue, tempStartTime, tempEndTime));
                }
            }
        } catch (Exception e) {
            logger.warning("Exception caught in parsing JSONObject");
        }
        return listOfModuleSlots;
    }

    /**
     * Processes the given timetable for viewing.
     *
     * @param timetable to view
     * @return an ArrayList containing the {@code TimetableModuleSlots} from the Timetable.
     */
    public static ArrayList<TimetableModuleSlot> setUpUnsortedModuleSlotsForViewing(Timetable timetable) {
        ArrayList<TimetableModuleSlot> allUnsortedModulesSlots = timetable.getAllModulesSlots();

        for (TimetableModuleSlot t : allUnsortedModulesSlots) {
            t.setComparing(false);
        }
        return allUnsortedModulesSlots;
    }

    /**
     * Combines the two lists of {@code TimetableModuleSlots} from each timetable and process them for comparing.
     *
     * @param first  Timetable to compare
     * @param second Timetable to compare
     * @return an ArrayList containing the combined {@code TimetableModuleSlots} from both Timetables.
     */
    public static ArrayList<TimetableModuleSlot> setUpUnsortedModuleSlotsForComparing(Timetable first,
                                                                                      Timetable second) {
        ArrayList<TimetableModuleSlot> allUnsortedModulesSlots = new ArrayList<TimetableModuleSlot>();
        allUnsortedModulesSlots.addAll(first.getAllModulesSlots());
        allUnsortedModulesSlots.addAll(second.getAllModulesSlots());

        for (TimetableModuleSlot t : allUnsortedModulesSlots) {
            t.setComparing(true);
        }
        return allUnsortedModulesSlots;
    }

    /**
     * Sorts TimetableModuleSlots
     *
     * @return HashMap of {@code Day}, {@code list of TimetableModuleSlots sorted by time}
     */
    public static HashMap<String, ArrayList<TimetableModuleSlot>> sortModuleSlotsByDay(
            ArrayList<TimetableModuleSlot> unsortedTimetableModuleSlots) {
        ArrayList<ArrayList<TimetableModuleSlot>> listOfDays = new ArrayList<ArrayList<TimetableModuleSlot>>();

        // add ArrayList for Monday to Friday
        listOfDays.add(new ArrayList<TimetableModuleSlot>());
        listOfDays.add(new ArrayList<TimetableModuleSlot>());
        listOfDays.add(new ArrayList<TimetableModuleSlot>());
        listOfDays.add(new ArrayList<TimetableModuleSlot>());
        listOfDays.add(new ArrayList<TimetableModuleSlot>());

        for (TimetableModuleSlot t : unsortedTimetableModuleSlots) {
            try {
                listOfDays.get(convertDayToInteger(t.getDay())).add(t);
            } catch (IllegalValueException e) {
                logger.warning("Invalid day entered");
            }
        }

        for (ArrayList<TimetableModuleSlot> t : listOfDays) {
            ArrayList<TimetableModuleSlot> temp = sortModuleSlotsByTime(t);
            t.clear();
            t.addAll(temp);
        }

        HashMap<String, ArrayList<TimetableModuleSlot>> sortedTimetableModuleSlots =
                new HashMap<String, ArrayList<TimetableModuleSlot>>();
        sortedTimetableModuleSlots.put(DAYS[MONDAY_INDEX], listOfDays.get(MONDAY_INDEX));
        sortedTimetableModuleSlots.put(DAYS[TUESDAY_INDEX], listOfDays.get(TUESDAY_INDEX));
        sortedTimetableModuleSlots.put(DAYS[WEDNESDAY_INDEX], listOfDays.get(WEDNESDAY_INDEX));
        sortedTimetableModuleSlots.put(DAYS[THURSDAY_INDEX], listOfDays.get(THURSDAY_INDEX));
        sortedTimetableModuleSlots.put(DAYS[FRIDAY_INDEX], listOfDays.get(FRIDAY_INDEX));
        return sortedTimetableModuleSlots;
    }

    /**
     * Sorts Module Slots by time
     * @param timetableModuleSlots
     * @return a list of sorted TimetableModuleSlots
     */
    private static ArrayList<TimetableModuleSlot> sortModuleSlotsByTime(
            ArrayList<TimetableModuleSlot> timetableModuleSlots) {
        Collections.sort(timetableModuleSlots);
        return splitIntoHalfHourSlots(timetableModuleSlots);
    }

    /**
     * Splits each TimetableModuleSlots into half hour slots and adds empty slots to represent breaks
     *
     * @param timetableModuleSlots the ArrayList to split into half hour slots
     * @return an ArrayList of TimetableModuleSlot with each slot representing one half-hour slot in the timetable
     */
    private static ArrayList<TimetableModuleSlot> splitIntoHalfHourSlots(
            ArrayList<TimetableModuleSlot> timetableModuleSlots) {
        ArrayList<TimetableModuleSlot> filled = new ArrayList<TimetableModuleSlot>();

        int j = 0;
        for (int i = 0; i < TIMES.length; i++) {
            if (j < timetableModuleSlots.size() && timetableModuleSlots.get(j).getStartTime().equals(TIMES[i])) {
                while (!timetableModuleSlots.get(j).getEndTime().equals(TIMES[i])) {
                    filled.add(timetableModuleSlots.get(j));
                    i++;
                }
                j++;
                i--;
            } else if (j < timetableModuleSlots.size()
                    && Integer.valueOf(timetableModuleSlots.get(j).getStartTime()) < Integer.valueOf(TIMES[i])
                    && Integer.valueOf(timetableModuleSlots.get(j).getEndTime()) > Integer.valueOf(TIMES[i])) {
                while (!timetableModuleSlots.get(j).getEndTime().equals(TIMES[i])) {
                    filled.add(timetableModuleSlots.get(j));
                    i++;
                }
                j++;
                i--;
            } else {
                filled.add(new TimetableModuleSlot());
            }
        }
        return filled;
    }

    // ================ Helper methods ==============================

    /**
     * Converts shortened lesson type from URL to long lesson type in API
     * @param lessonType shortened lesson type from URL
     * @return long lesson type in API
     */
    private static String convertLessonType(String lessonType) throws IllegalValueException {
        switch (lessonType) {
        case "LEC":
            return "Lecture";

        case "TUT":
            return "Tutorial";

        case "LAB":
            return "Laboratory";

        case "SEM":
            return "Seminar-Style Module Class";

        case "SEC":
            return "Sectional Teaching";

        case "REC":
            return "Recitation";

        case "TUT2":
            return "Tutorial Type 2";

        case "TUT3":
            return "Tutorial Type 3";

        default:
            throw new IllegalValueException(MESSAGE_INVALID_CLASS_TYPE);
        }
    }

    /**
     * Converts {@code Day} to Integer for comparative purposes
     * @param day in String
     * @return int representing day
     */
    public static int convertDayToInteger(String day) throws IllegalValueException {
        switch (day.toUpperCase()) {
        case "MONDAY":
            return MONDAY_INDEX;

        case "TUESDAY":
            return TUESDAY_INDEX;

        case "WEDNESDAY":
            return WEDNESDAY_INDEX;

        case "THURSDAY":
            return THURSDAY_INDEX;

        case "FRIDAY":
            return FRIDAY_INDEX;

        default:
            throw new IllegalValueException(MESSAGE_INVALID_DAY_TYPE);
        }
    }
}
