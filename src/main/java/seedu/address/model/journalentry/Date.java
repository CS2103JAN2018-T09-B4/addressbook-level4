package seedu.address.model.journalentry;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Journal Entry's Date in the journal.
 */
public class Date {


    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Date can only contain numbers, and must be 8 digits long";
    public static final String DATE_VALIDATION_REGEX = "\\d{8}";
    public final String value;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        this.value = date;
    }

    /**
     * Returns true if a given string is a valid person phone number.
     */
    public static boolean isValidDate(String test) {
        return test.matches(DATE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Date // instanceof handles nulls
                && this.value.equals(((Date) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(value);
    }

}
