# chenxing1992
###### \java\seedu\address\logic\Logic.java
``` java
    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<ReadOnlyPerson> getFilteredPersonList();

    /** Returns an unmodifiable view of the list of journal entries */
    ObservableList<JournalEntry> getJournalEntryList();

    /** Returns the list of input entered by the user, encapsulated in a {@code ListElementPointer} object */
    ListElementPointer getHistorySnapshot();
}
```
###### \java\seedu\address\logic\LogicManager.java
``` java
    @Override
    public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<JournalEntry> getJournalEntryList() {
        return model.getJournal().getJournalEntryList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }
}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     *
     * Help to remove unwanted tags
     */
    public void removeUnusedTags(Set<Tag> tagToRemove) {
        Set<Tag> newTags = getTagsExcluding(tagToRemove);
        tags.setTags(newTags);
        syncMasterTagListWith(persons);
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     *
     * Help to exclude unwanted tags
     */
    public Set<Tag> getTagsExcluding(Set<Tag> tagsToExclude) {
        Set<Tag> output = tags.toSet();
        for (Tag tagExcluded : tagsToExclude) {
            output.remove(tagExcluded);
        }
        return output;
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Make sure that these people:
     * - appear in the master list {@link #tags}
     * - Tag objects are pointed in the master list
     *
     * @see #syncMasterTagListWith(Person)
     */
    private void syncMasterTagListWith(UniquePersonList persons) {
        persons.forEach(this::syncMasterTagListWith);
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Make sure this person:
     * - Appear in the master list {@link #tags}
     * - Tag object is pointed in the master list
     */
    private void syncMasterTagListWith(Person person) {
        final UniqueTagList Tags = new UniqueTagList(person.getTags());
        tags.mergeFrom(Tags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> mainTagObjects = new HashMap<>();
        tags.forEach(tag -> mainTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        Tags.forEach(tag -> correctTagReferences.add(mainTagObjects.get(tag)));
        person.setTags(correctTagReferences);
    }

    /**
     *  Updates the master tag list to include tags in {@code person} that are not in the list.
     *  @return a copy of this {@code person} such that every tag in this person points to a Tag object in the master
     *  list.
     */
    private ReadOnlyPerson syncWithMasterTagList(ReadOnlyPerson person) {
        final UniqueTagList personTags = new UniqueTagList(person.getTags());
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new Person(
                person.getName(), person.getPhone(), person.getEmail(), person.getAddress(), person.getTimetable(),
                correctTagReferences);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * @throws PersonNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePerson(ReadOnlyPerson key) throws PersonNotFoundException {
        if (persons.remove(key)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    /**
     * Removes {@code tag} from {@code person} in this {@code AddressBook}.
     * @throws PersonNotFoundException if the {@code person} is not in this {@code AddressBook}.
     */
    private void removeTagFromPerson(Tag tag, Person person) throws PersonNotFoundException {
        Set<Tag> newTags = new HashSet<>(person.getTags());

        if (!newTags.remove(tag)) {
            return;
        }

        Person newPerson =
                new Person(person.getName(), person.getPhone(), person.getEmail(), person.getAddress(),
                        person.getTimetable(), newTags);

        try {
            updatePerson(person, newPerson);
        } catch (DuplicatePersonException dpe) {
            throw new AssertionError("Removing a person's tags should not result in a duplicate. "
                    + "See Person#equals(Object).");
        }
    }

    /**
     * Removes {@code tag} from all persons in this {@code AddressBook}.
     */
    public void removeTag(Tag tag) {
        try {
            for (Person person : persons) {
                removeTagFromPerson(tag, person);
            }
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("Impossible: original person is obtained from the address book.");
        }
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<ReadOnlyPerson> getPersonList() {
        return persons.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.persons.equals(((AddressBook) other).persons)
                && this.tags.equalsOrderInsensitive(((AddressBook) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, tags);
    }
}
```
###### \java\seedu\address\model\person\Person.java
``` java
/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person implements ReadOnlyPerson {

    private ObjectProperty<Name> name;
    private ObjectProperty<Phone> phone;
    private ObjectProperty<Email> email;
    private ObjectProperty<Address> address;
    private ObjectProperty<Timetable> timetable;
    private ObjectProperty<UniqueTagList> tags;


```
###### \java\seedu\address\model\person\Person.java
``` java
    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address,
                  Timetable timetable, Set<Tag> tags) {

        requireAllNonNull(name, phone, email, address, timetable, tags);

        this.name = new SimpleObjectProperty<>(name);
        this.phone = new SimpleObjectProperty<>(phone);
        this.email = new SimpleObjectProperty<>(email);
        this.address = new SimpleObjectProperty<>(address);
        this.timetable = new SimpleObjectProperty<>(timetable);
        // protect internal tags from changes in the arg list
        this.tags = new SimpleObjectProperty<>(new UniqueTagList(tags));

    }

```
###### \java\seedu\address\model\person\Person.java
``` java
    /**
     * Creates a copy of the given ReadOnlyPerson.
     */
    public Person(ReadOnlyPerson source) {
        this(source.getName(), source.getPhone(), source.getEmail(), source.getAddress(),
                source.getTimetable(), source.getTags());
    }
    public void setName(Name name) {
        this.name.set(requireNonNull(name));
    }

    @Override
    public ObjectProperty<Name> nameProperty() {
        return name;
    }

    @Override
    public Name getName() {
        return name.get();
    }

    public void setPhone(Phone phone) {
        this.phone.set(requireNonNull(phone));
    }

    @Override
    public ObjectProperty<Phone> phoneProperty() {
        return phone;
    }

    @Override
    public Phone getPhone() {
        return phone.get();
    }

    public void setEmail(Email email) {
        this.email.set(requireNonNull(email));
    }

    @Override
    public ObjectProperty<Email> emailProperty() {
        return email;
    }

    @Override
    public Email getEmail() {
        return email.get();
    }

    public void setAddress(Address address) {
        this.address.set(requireNonNull(address));
    }

    @Override
    public ObjectProperty<Address> addressProperty() {
        return address;
    }

    @Override
    public Address getAddress() {
        return address.get();
    }

    public void setTimetable(Timetable timetable) {
        this.timetable.set(requireNonNull(timetable));
    }

    @Override
    public ObjectProperty<Timetable> timeTableProperty() {
        return timetable;
    }

    @Override
    public Timetable getTimetable() {
        return timetable.get();
    }
    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    @Override
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.get().toSet());
    }

    public ObjectProperty<UniqueTagList> tagProperty() {
        return tags;
    }

    /**
     * Replaces this person's tags with the tags in the argument tag set.
     */
    public void setTags(Set<Tag> replacement) {
        tags.set(new UniqueTagList(replacement));
    }

    public boolean hasTag(Tag tag) {
        return tags.get().contains(tag);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyPerson // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyPerson) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, timetable, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

}

```
###### \java\seedu\address\model\person\ReadOnlyPerson.java
``` java
/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyPerson {

    ObjectProperty<Name> nameProperty();
    Name getName();
    ObjectProperty<Phone> phoneProperty();
    Phone getPhone();
    ObjectProperty<Email> emailProperty();
    Email getEmail();
    ObjectProperty<Address> addressProperty();
    Address getAddress();
    ObjectProperty<Timetable> timeTableProperty();
    Timetable getTimetable();
    ObjectProperty<UniqueTagList> tagProperty();
    Set<Tag> getTags();
    //ObjectProperty<AppointmentList> appointmentProperty();
    // List<Appointment> getAppointments();
    /**Same state detected will return true.
     */
    default boolean isSameStateAs(ReadOnlyPerson rp) {
        return rp == this // short circuit if same object
                || (rp != null // this is first to avoid NPE below
                && rp.getName().equals(this.getName()) // state checks here onwards
                && rp.getPhone().equals(this.getPhone())
                && rp.getEmail().equals(this.getEmail())
                && rp.getAddress().equals(this.getAddress())
                && rp.getTimetable().equals((this.getTimetable())));
    }

```
###### \java\seedu\address\model\person\ReadOnlyPerson.java
``` java
    /**
     * Show all contact in detail as text
     */
    default String getAsText() {
        final StringBuilder b = new StringBuilder();
        b.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" TimeTable: ")
                .append(getTimetable())
                .append(" Tags: ");
        getTags().forEach(b::append);
        return b.toString();
    }

}

```
###### \java\seedu\address\model\person\UniquePersonList.java
``` java
    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ReadOnlyPerson> asObservableList() {
        return FXCollections.unmodifiableObservableList(mappedList);
    }

```
###### \java\seedu\address\model\person\UniquePersonList.java
``` java
    /**
     * @return the list as an unmodifiable list and sorted by name in ascending order
     */
    public ObservableList<ReadOnlyPerson> asObservableListSortedByNameAsc() {
        internalList.sort((o1, o2) -> {
            int output = (o1.getName().fullName.compareToIgnoreCase(o2.getName().fullName) >= 0) ? 1 : -1;
            return output;
        });
        return FXCollections.unmodifiableObservableList(mappedList);
    }

```
###### \java\seedu\address\model\person\UniquePersonList.java
``` java
    /**
     * @return a unmodifiable list and will be sorted by name in descending order
     */
    public ObservableList<ReadOnlyPerson> asObservableListSortedByNameDsc() {
        internalList.sort((o1, o2) -> {
            int op = (o1.getName().fullName.compareToIgnoreCase(o2.getName().fullName) <= 0) ? 1 : -1;
            return op;
        });
        return FXCollections.unmodifiableObservableList(mappedList);
    }
```
###### \java\seedu\address\model\person\UniquePersonList.java
``` java
    /**
     * @return list is reversed
     */
    public ObservableList<ReadOnlyPerson> asObservableListReversed() {
        FXCollections.reverse(internalList);
        return FXCollections.unmodifiableObservableList(mappedList);
    }


    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePersonList // instanceof handles nulls
                && this.internalList.equals(((UniquePersonList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }


    /**
     * Util method to extract person out from a list
     */
    private Person getPerson(ReadOnlyPerson target) throws PersonNotFoundException {
        requireNonNull(target);
        for (Person person : internalList) {
            if (person.equals(target)) {
                return person;
            }
        }
        throw new PersonNotFoundException();
    }

}

```
###### \resources\view\CalendarDisplay.fxml
``` fxml
<?import javafx.scene.control.TextArea?>
<?import javafx.scene.layout.StackPane?>

<StackPane fx:id="placeHolder" styleClass="pane-with-border" xmlns="http://javafx.com/javafx/9.0.4" xmlns:fx="http://javafx.com/fxml/1">
  <TextArea fx:id="resultDisplay" editable="false" prefHeight="416.0" prefWidth="796.0" style="-fx-background-color: yellow;" styleClass="result-display" />
</StackPane>
```
