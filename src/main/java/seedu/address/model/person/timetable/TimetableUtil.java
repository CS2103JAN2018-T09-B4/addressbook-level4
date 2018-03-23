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
import java.util.Iterator;
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

/**
 * A class containing utility methods for Timetable
 */
public class TimetableUtil {
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
    private static final int MONDAY_INDEX = 0;
    private static final int TUESDAY_INDEX = 1;
    private static final int WEDNESDAY_INDEX = 2;
    private static final int THURSDAY_INDEX = 3;
    private static final int FRIDAY_INDEX = 4;

    /**
     * Sets up attributes of a given {@code Timetable}.
     * @param timetable Timetable to be set up
     */
    public static void setUpTimetableInfo(Timetable timetable) {
        try {
            setAndExpandShortTimetableUrl(timetable);
            setSemNumFromExpandedUrl(timetable);
            splitLongTimetableUrl(timetable);
            getModuleInfoFromApi(timetable);
        } catch (ParseException e) {
            logger.warning(MESSAGE_INVALID_SHORT_URL);
        }
    }

    /**
     * Expands short NUSMods timetable URL to a long NUSMods timetable URL from {@timetable}.
     * Sets the expanded URL for {@code timetable}.
     */
    public static void setAndExpandShortTimetableUrl(Timetable timetable) throws ParseException {
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
        timetable.setExpandedUrl(expandedUrl);
    }

    /**
     * Parses for {@code currentSemester} from expandedUrl and sets it for {@code timetable}
     * @param timetable whose {@code currentSemester} is to be set
     */
    public static void setSemNumFromExpandedUrl(Timetable timetable) {
        String expandedUrl = timetable.getExpandedUrl();
        requireNonNull(expandedUrl);
        String[] moduleInformation = expandedUrl.split(SPLIT_QUESTION_MARK);
        timetable.setCurrentSemester(Integer.valueOf(moduleInformation[SEM_NUMBER_INDEX]
                .replaceAll(REPLACE_NON_DIGIT_CHARACTERS, "")));
    }

    /**
     * Splits expanded NUSMods timetable URL into the different {@code TimetableModule}s.
     * Sets listOfModules in {@code timetable}
     * Expanded timetable URL is in the format ...?[MODULE_CODE]=[LESSON_TYPE]:[CLASS_NUM]&...
     * @param timetable whose long url is to be split
     * @return ArrayList of {@code TimetableModule}
     */
    public static HashMap<String, TimetableModule> splitLongTimetableUrl(Timetable timetable) {
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
        timetable.setListOfModules(listOfModules);
        return listOfModules;
    }

    public static void getModuleInfoFromApi(Timetable timetable) {
        requireNonNull(timetable.getListOfModules());

        String currentModuleInfo;
        ArrayList<TimetableModuleSlot> allTimetableModuleSlots = null;
        Set entrySet = timetable.getListOfModules().entrySet();
        Iterator it = entrySet.iterator();

        while (it.hasNext()) {
            Map.Entry currentModule = (Map.Entry) it.next();
            currentModuleInfo = getJsonContentsFromNusModsApi("2017-2018",
                    Integer.toString(timetable.getCurrentSemester()), currentModule.getKey().toString());
            allTimetableModuleSlots = getAllTimetableModuleSlots(currentModuleInfo, timetable,
                    currentModule.getKey().toString());
        }
        timetable.setListOfDays(sortModuleSlotsByDay(allTimetableModuleSlots));
    }

    /**
     * Retrieves json file from NUSMods api and converts to String
     * @param acadYear String representing academic year
     * @param semNum String representing semester number
     * @param moduleCode String representing module code
     * Format: http://api.nusmods.com/[acadYear]/[semNum]/modules/[moduleCode]/timetable.json
     * e.g. http://api.nusmods.com/2017-2018/2/modules/CS3241/timetable.json
     * @return String containing contents of json file
     */
    public static String getJsonContentsFromNusModsApi(String acadYear, String semNum, String moduleCode) {
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
     * Read the responded result
     * @param inputStream from nusmods api
     * @return string containing contents of nusmods api
     * @throws IOException from readLine()
     */
    public static String readStream(InputStream inputStream) throws IOException {
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

        try {
            Object obj = parser.parse(contents);
            jsonObject = (JSONObject) obj;
        } catch (Exception e) {
            logger.warning("Exception caught in parsing JSONObject");
        }

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

        ArrayList<TimetableModuleSlot> listOfModuleSlots = new ArrayList<TimetableModuleSlot>();
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
        return listOfModuleSlots;
    }

    /**
     * Sorts TimetableModuleSlots
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
            t = sortModuleSlotsByTime(t);
        }

        HashMap<String, ArrayList<TimetableModuleSlot>> sortedTimetableModuleSlots =
                new HashMap<String, ArrayList<TimetableModuleSlot>>();
        sortedTimetableModuleSlots.put("MONDAY", listOfDays.get(MONDAY_INDEX));
        sortedTimetableModuleSlots.put("TUESDAY", listOfDays.get(TUESDAY_INDEX));
        sortedTimetableModuleSlots.put("WEDNESDAY", listOfDays.get(WEDNESDAY_INDEX));
        sortedTimetableModuleSlots.put("THURSDAY", listOfDays.get(THURSDAY_INDEX));
        sortedTimetableModuleSlots.put("FRIDAY", listOfDays.get(FRIDAY_INDEX));
        return sortedTimetableModuleSlots;
    }

    /**
     * Sorts Module Slots by Time
     * @param timetableModuleSlots
     * @return
     */
    public static ArrayList<TimetableModuleSlot> sortModuleSlotsByTime(
            ArrayList<TimetableModuleSlot> timetableModuleSlots) {
        Collections.sort(timetableModuleSlots);
        return timetableModuleSlots;
    }

    /**
     * Converts shortened lesson type from URL to long lesson type in API
     * @param lessonType shortened lesson type from URL
     * @return long lesson type in API
     */
    public static String convertLessonType(String lessonType) throws IllegalValueException {
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
        switch (day.toLowerCase()) {
        case "monday":
            return 0;

        case "tuesday":
            return 1;

        case "wednesday":
            return 2;

        case "thursday":
            return 3;

        case "friday":
            return 4;

        default:
            throw new IllegalValueException(MESSAGE_INVALID_DAY_TYPE);
        }
    }
}
