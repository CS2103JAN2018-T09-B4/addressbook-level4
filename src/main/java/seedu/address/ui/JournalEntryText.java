package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

public class JournalEntryText extends UiPart<Region> {

    private static final String FXML = "JournalEntryText.fxml";

    @FXML
    private TextArea journalTextArea;

    public JournalEntryText() {
        super(FXML);
    }

    public String getText() {
        return journalTextArea.getText();
    }

}
