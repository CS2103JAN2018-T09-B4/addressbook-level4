package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.journalentry.JournalEntry;

//@@author traceurgan
/**
 * An UI component that displays information of a {@code Person}.
 */
public class JournalEntryCard extends UiPart<Region> {

    private static final String FXML = "JournalEntryListCard.fxml";

    public final JournalEntry journalEntry;

    @FXML
    private HBox cardPane;
    @FXML
    private Label date;
    @FXML
    private Label id;
    @FXML
    private Label text;

    public JournalEntryCard(JournalEntry journalEntry, int displayedIndex) {
        super(FXML);
        this.journalEntry = journalEntry;
        id.setText(displayedIndex + ". ");
        date.setText(journalEntry.getDate().value);
        text.setText(journalEntry.getText());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof JournalEntryCard)) {
            return false;
        }

        // state check
        JournalEntryCard card = (JournalEntryCard) other;
        return id.getText().equals(card.id.getText())
                && journalEntry.equals(card.journalEntry);
    }
}
