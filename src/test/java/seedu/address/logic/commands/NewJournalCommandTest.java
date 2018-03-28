package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.logic.commands.NewJournal.NEW_JOURNAL_ENTRY_CREATED;

import org.junit.Test;

public class NewJournalCommandTest {

    @Test
    public void execute_newjournal_success() {
        CommandResult result = new NewJournal().execute();
        assertEquals(NEW_JOURNAL_ENTRY_CREATED, result.feedbackToUser);
    }
}
