# HEARTOFAL1ON
###### \java\seedu\address\model\locationfinder\LocationFinder.java
``` java
/**
 * Represents a Google Maps location search query
 */
public class LocationFinder {

    public static final String MESSAGE_LOCATION_FINDER_CONSTRAINTS = "A Google Maps location search query"
            + " should be of the following format https://www.google.com/maps/search/?api=1&query= and"
            + " adhere to the following constraints:\n"
            + "1. The URL should start with https://www.google.com/maps/search/?api=1&query=\n"
            + "2. The part after that can take any values, and it should not be blank. It also has to be a"
            + " URL-escaped string but Java API has a dedicated URLEncoder class which contains methods to"
            + " convert a string into HTML format.";
    private static final String GOOGLE_MAPS_SEARCH_URL_REGEX = "https://www.google.com/maps/search/?api=1&query=";

    /*
     * The first character of the parameters must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    private static final String QUERY_VALIDATION_REGEX = "[^\\s].*";

    public static final String LOCATION_FINDER_VALIDATION_REGEX = GOOGLE_MAPS_SEARCH_URL_REGEX
            + QUERY_VALIDATION_REGEX;
    public final String value;
    private String url;

    /**
     * Constructs an {@code Address}.
     *
     * @param locationQuery A valid locationQuery.
     */
    public LocationFinder(String locationQuery) {
        requireNonNull(locationQuery);
        checkArgument(isValidLocationQuery(locationQuery), MESSAGE_LOCATION_FINDER_CONSTRAINTS);
        this.value = locationQuery;
    }

    public String getLocationFinderUrl() {
        return url;
    }

    public void setLocationFinderUrl(String url) {
        this.url = url;
    }

    /**
     * Returns true if a given string is a valid location query.
     */
    public static boolean isValidLocationQuery(String test) {
        return test.matches(LOCATION_FINDER_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LocationFinder // instanceof handles nulls
                && this.value.equals(((LocationFinder) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\address\model\locationfinder\LocationFinderUtil.java
``` java
/**
 * A class containing utility methods for LocationFinder
 */
public class LocationFinderUtil {

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    private static final String INVALID_URL_RESULT = "https://www.google.com/maps/search/?api=1&query=";
    private static final String MESSAGE_INVALID_URL = "Invalid Google Maps search query URL provided.";

    /**
     * Sets up attributes of a given {@code LocationFinder}.
     * @param locationFinder LocationFinder to be set up
     */
    public static void setUpTimetableInfo(LocationFinder locationFinder) {
        try {
            setLocationFinderUrl(locationFinder);
            setLocationFinderUrl(locationFinder);
            getResultFromApi(locationFinder);
        } catch (ParseException e) {
            logger.warning(MESSAGE_INVALID_URL);
        }
    }

    /**
     * Sets the URL for {@code locationFinder}.
     */
    public static void setLocationFinderUrl(LocationFinder locationFinder) throws ParseException {
        String locationFinderUrl = locationFinder.value;
        String finalUrl = null;
        checkArgument(LocationFinder.isValidLocationQuery(locationFinderUrl),
                LocationFinder.MESSAGE_LOCATION_FINDER_CONSTRAINTS);

        try {
            URLEncoder.encode(locationFinderUrl);
            final URL lFUrl = new URL(locationFinderUrl);
            final HttpsURLConnection urlConnection = (HttpsURLConnection) lFUrl.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            finalUrl = urlConnection.getHeaderField("address");

            if (finalUrl.equals(INVALID_URL_RESULT)) {
                throw new ParseException(MESSAGE_INVALID_URL);
            }
        } catch (MalformedURLException e) {
            logger.warning("URL provided is malformed");
        } catch (IOException e) {
            logger.warning("Failed to open connection");
        }
        locationFinder.setLocationFinderUrl(finalUrl);
    }

    public static void getResultFromApi(LocationFinder locationFinder) {
        // dummy method
    }

    /**
     * Retrieves json file from Google Maps API and converts to String
     */
    public static String getJsonContentsFromNusModsApi() {
        // dummy method
        return null;
    }

    /**
     * Read the responded result
     * @throws IOException from readLine()
     */
    public static String readStream(InputStream inputStream) throws IOException {
        // dummy method
        return null;
    }

}
```
###### \java\seedu\address\model\locationfinder\LocationQuery.java
``` java
/**
 * Represents a location query to the Google Maps http server which helps to get a location result
 */
public class LocationQuery {

    private String locationQuery;
    private HashMap<String, String> locationResult; // Key is locationQuery type, Value is locationResult type

    public LocationQuery(String locationQuery, HashMap<String, String> locationResult) {
        this.locationQuery = locationQuery;
        this.locationResult = locationResult;
    }

    public String getLocationQuery() {
        return locationQuery;
    }

    public HashMap<String, String> getLocationResult() {
        return locationResult;
    }

    @Override
    public boolean equals(Object other) {
        return (other == this // short circuit if same object
                || (other instanceof LocationQuery // instanceof handles nulls
                && this.locationQuery.equals(((LocationQuery) other).locationQuery)
                && this.locationResult.equals(((LocationQuery) other).locationResult))); // state check
    }

}
```
