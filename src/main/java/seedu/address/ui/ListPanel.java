package seedu.address.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.HideTimetableRequestEvent;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.journalentry.JournalEntry;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Panel containing the list of persons.
 */
public class ListPanel extends UiPart<Region> {
    private static final String FXML = "ListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ListPanel.class);

    @FXML
    private ListView<PersonCard> personListView;

    @FXML
    private ListView<JournalEntryCard> journalEntryListView;

    public ListPanel(ObservableList<ReadOnlyPerson> personList, ObservableList<JournalEntry> journalEntryList) {
        super(FXML);
        setConnections(personList, journalEntryList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(
            ObservableList<ReadOnlyPerson> personList, ObservableList<JournalEntry> journalEntryList) {
        ObservableList<PersonCard> mappedList = EasyBind.map(
                personList, person -> new PersonCard(person, personList.indexOf(person) + 1));

        ObservableList<JournalEntryCard> mappedListToo = EasyBind.map(journalEntryList, journalEntry ->
                new JournalEntryCard(journalEntry, journalEntryList.indexOf(journalEntry) + 1));
        personListView.setItems(mappedList);
        journalEntryListView.setItems(mappedListToo);
        personListView.setCellFactory(listView -> new PersonListViewCell());
        journalEntryListView.setCellFactory(listView -> new JournalEntryListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        personListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in person list panel changed to : '" + newValue + "'");
                        raise(new PersonPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            personListView.scrollTo(index);
            personListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Deselects the {@code PersonCard} at the {@code index}.
     */
    private void deselect(int index) {
        Platform.runLater(() -> {
            personListView.getSelectionModel().clearSelection();
        });
    }

    @Subscribe
    private void handleHideTimetableRequestEvent (HideTimetableRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        deselect(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<PersonCard> {

        @Override
        protected void updateItem(PersonCard person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(person.getRoot());
            }
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PersonCard}.
     */
    class JournalEntryListViewCell extends ListCell<JournalEntryCard> {

        @Override
        protected void updateItem(JournalEntryCard journalEntryCard, boolean empty) {
            super.updateItem(journalEntryCard, empty);

            if (empty || journalEntryCard == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(journalEntryCard.getRoot());
            }
        }
    }

}
