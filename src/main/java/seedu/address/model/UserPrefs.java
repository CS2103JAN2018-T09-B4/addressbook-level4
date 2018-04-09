package seedu.address.model;

import java.util.Objects;

import seedu.address.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private GuiSettings guiSettings;
    private String personFilePath = "data/person.xml";
    private String journalFilePath = "data/journal.xml";
    private String nusCouplesName = "NUSCouples";

    public UserPrefs() {
        this.setGuiSettings(500, 500, 0, 0);
    }

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }

    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
    }

    public String getPersonFilePath() {
        return personFilePath;
    }

    public String getJournalFilePath() {
        return journalFilePath;
    }

    public void setPersonFilePath(String personFilePath) {
        this.personFilePath = personFilePath;
    }

    public String getNuscouplesName() {
        return nusCouplesName;
    }

    public void setNuscouplesName(String nuscouplesName) {
        this.nusCouplesName = nuscouplesName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return Objects.equals(guiSettings, o.guiSettings)
                && Objects.equals(personFilePath, o.personFilePath)
                && Objects.equals(journalFilePath, o.journalFilePath)
                && Objects.equals(nusCouplesName, o.nusCouplesName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, personFilePath, nusCouplesName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings.toString());
        sb.append("\nLocal person data file location : " + personFilePath);
        sb.append("\nLocal journal data file location : " + journalFilePath);
        sb.append("\nNUSCouples name : " + nusCouplesName);
        return sb.toString();
    }

}
