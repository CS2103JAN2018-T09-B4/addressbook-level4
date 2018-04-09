package seedu.address.ui;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;
import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.PersonChangedEvent;
import seedu.address.commons.events.ui.CalendarViewEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Appointment.Appointment;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String TIMETABLE_PAGE = "TimetablePage.html";
    public static final String SEARCH_PAGE_URL =
            "https://se-edu.github.io/addressbook-level4/DummySearchPage.html?name=";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    @FXML
    private CalendarView calendarView;
    private ObservableList<ReadOnlyPerson> personList;

    //@@author chenxing1992
    public BrowserPanel(ObservableList<ReadOnlyPerson> personList) {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        //  getRoot().setOnKeyPressed(Event::consume);

        this.personList = personList;

        calendarView = new CalendarView();
        calendarView.setRequestedTime(LocalTime.now());
        calendarView.setToday(LocalDate.now());
        calendarView.setTime(LocalTime.now());
        updateCalendar();
        disableViews();
        registerAsAnEventHandler(this);

        //loadDefaultPage();
        //registerAsAnEventHandler(this);
    }

    //@@author chenxing1992

    /**
     * Remove clutter from interface
     */
    private void disableViews() {
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowSearchField(false);
        calendarView.setShowSearchResultsTray(false);
        calendarView.setShowPrintButton(false);
        calendarView.showDayPage();
    }

    /**
     * Explicitly set the Root object to CalendarView
     */
    //@@author chenxing1992
    public CalendarView getCalendarRoot() {
        return this.calendarView;
    }

    /**
     * Changes calendar view accordingly
     */
    private void showPage(Character c) {
        switch(c) {
        case ('d'):
            calendarView.showDayPage();
            return;
        case ('w'):
            calendarView.showWeekPage();
            return;
        case ('m'):
            calendarView.showMonthPage();
            return;
        case ('y'):
            calendarView.showYearPage();
            return;
        default:
        //should not reach here
        assert (false);
        }
    }
    //@@author chenxing1992
    private void setTime() {
        calendarView.setToday(LocalDate.now());
        calendarView.setTime(LocalTime.now());
        calendarView.getCalendarSources().clear();
    }

    //@@author chenxing1992
    private Calendar getCalendar(int styleNum, ReadOnlyPerson person) {
        Calendar calendar = new Calendar(person.getName().toString());
        calendar.setStyle(Calendar.Style.getStyle(styleNum));
        calendar.setLookAheadDuration(Duration.ofDays(365));
        return calendar;
    }

    //@@author chenxing1992
    private ArrayList<Entry> getEntries(ReadOnlyPerson person) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (Appointment appointment : person.getAppointments()) {
            LocalDateTime ldtstart = LocalDateTime.ofInstant(appointment.getDate().toInstant(),
                    ZoneId.systemDefault());
            LocalDateTime ldtend = LocalDateTime.ofInstant(appointment.getEndDate().toInstant(),
                    ZoneId.systemDefault());

            entries.add(new Entry(appointment.getDescription() + " with " + person.getName(),
                    new Interval(ldtstart, ldtend)));
        }
        return entries;
    }

    //@@author chenxing1992

    /**
     * Creates a new a calendar with the update information
     */
    private void updateCalendar() {
        setTime();
        CalendarSource calendarSource = new CalendarSource("Appointments");
        int styleNum = 0;
        for (ReadOnlyPerson person : personList) {
            Calendar calendar = getCalendar(styleNum, person);
            calendarSource.getCalendars().add(calendar);
            ArrayList<Entry> entries = getEntries(person);
            styleNum++;
            styleNum = styleNum % 5;
            for (Entry entry : entries) {
                calendar.addEntry(entry);
            }
        }
        calendarView.getCalendarSources().add(calendarSource);
    }
    //@@author chenxing1992
    @Subscribe
    private void handleCalendarViewEvent(CalendarViewEvent event) {
        Character c = event.c;
        Platform.runLater(() -> showPage(c));
    }

    //@@author marlenekoh
    /**
     * Loads the timetable page of a person into browser panel
     */
    public void loadTimetablePage() {
        URL timetablePage = MainApp.class.getResource(FXML_FILE_FOLDER + TIMETABLE_PAGE);
        loadPage(timetablePage.toExternalForm());
    }

    //@@author
    private void loadPersonPage(ReadOnlyPerson person) {
        loadPage(SEARCH_PAGE_URL + person.getName().fullName);
    }

    private void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    public void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

    @Subscribe
    private void handleNewAppointmentEvent(PersonChangedEvent event) {
        Platform.runLater(
                this::updateCalendar
        );

    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadTimetablePage();
    }
}
