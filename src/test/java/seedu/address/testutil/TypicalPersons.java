package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIMETABLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIMETABLE_BOB;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    //TODO: ZAC NEEED TO ADD HIS APPTS HERE
    // GO TO MODELMANAGERTEST AND UNIGNORE AND SHOULD BE OK
    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@gmail.com").withPhone("85355255")
            .withTimetable("http://modsn.us/wNuIW").withAppointment("Lunch, tomorrow 5pm to 7pm").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
            .withTimetable(VALID_TIMETABLE_AMY).withAppointment("Lunch, tomorrow 5pm to 7pm").build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
            .withTimetable(VALID_TIMETABLE_BOB).withAppointment("Lunch, tomorrow 5pm to 7pm").build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    public static ObservableList<ReadOnlyPerson> getPersonAsList() {
        List<ReadOnlyPerson> personList =  FXCollections.observableArrayList();
        personList.add(ALICE);
        return (ObservableList<ReadOnlyPerson>) personList;
    }

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static Person getTypicalPerson() {
        Person p = new Person(ALICE);

        return p;
    }
}
