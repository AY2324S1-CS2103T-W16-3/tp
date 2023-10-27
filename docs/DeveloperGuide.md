---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2324S1-CS2103T-W16-3/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2324S1-CS2103T-W16-3/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2324S1-CS2103T-W16-3/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2324S1-CS2103T-W16-3/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2324S1-CS2103T-W16-3/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2324S1-CS2103T-W16-3/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2324S1-CS2103T-W16-3/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

The `People` component,

* Has 6 compulsory attributes (`Name`, `Phone`, `Address`, `Email`, `Nric`, `LicencePlate`) and 1 optional attrbiute 
(`Remark`)
* Has `Tag` which can store any amount of tags
* Has a `Policy`, which is optional for a person to have, but is stored in the `Person` class regardless
  * If a person has a policy attached to them, `Policy` will simply hold their information (i.e. `PolicyNumber`, 
  policy issue and expiry date as `PolicyDate`)
  * If a person does not have a policy attached to them, `Policy` will hold default values (`NOPOLICY` for 
  `PolicyNumber` and `01-01-1000` for both `PolicyDate`)

#### Design considerations
  * Defensive programming was used for the handling of persons with no policy over the alternative implementation:
  * Make `Policy = null` for `Person` with no policy. This was unsafe as when `Person` is displayed in the UI, methods 
like `toString()` would throw errors, violating type safety.
  * Make `PolicyNumber` and `PolicyDate` be `null`. This was unsafe as the RegEx check (e.g. `isValidPolicyNumber()`)
  done in the constructors would have to be removed, leading to improper input validation.


<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2324S1-CS2103T-W16-3/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### _Features in InsureIQ_

### `batchdelete`

The `batchdelete` command allows users to batch delete people whose policy expiry is in the specified month and year.


#### Implementation:
The batch delete mechanism is facilitated by `DeleteMonth`

It is also facilitated by the operation:
* `ModelManager#batchDeleteWithPredicate(Predicate<Person> predicate)` - batch delete all people in address book who fulfil the given predicate.

This operation is exposed in the `Model` interface as `Model# batchDeleteWithPredicate(Predicate<Person> predicate)`

The following sequence diagram shows how the batch delete operation works:

![BatchDeleteSequenceDiagram1](images/BatchDeleteSequenceDiagram1.png)

* instance of DeleteMonth is constructed to construct a BatchDeleteCommand

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `BatchDeleteCommand` and `BatchDeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

![BatchDeleteSequenceDiagram2](images/BatchDeleteSequenceDiagram2.png)

* Calling method updateFilteredPersonList to get all people in the addressBook
* Using predicate p to get list of people to delete
* Delete all people in the list and return all people left in addressBook

#### Design consideration:
* Users may use batch delete to delete all people whose policy expiry is in the specified month and year. For example, delete people didn’t contact the user to renew their policies for one year. 
* Besides, if users leave an insurance company, they may like to delete people purchase policy from that company. 
* Therefore, `Model#batchDeleteWithPredicate(Predicate<Person> predicate)` is introduced to allow batch delete by month or company.

### `remind`
The `remind` command allows the user to filter out people whose policy expiry date is approaching within the given number of days.

#### Implementation:
The filtered list will be displayed in the UI. The remind mechanism is facilitated by `Model` through the following operations:
* `Model#RemindPredicate(int days)` - The Predicate to be used for filtering. `days` represents the number of days from the current date given by the user.
* `Model#updateFilteredPersonList(Predicate<Person> p)` - Filters the list of Persons to display by the Predicate `p`.

Given below is an example usage scenario and how the remind mechanism behaves at each step:

Step 1. The user realises that there may be a set of Persons whose policy expiry date is approaching within 30 days.

Step 2. The user executes `remind 30` command in an attempt to find this set of Persons. A `Model#RemindPredicate(int days)` instance with `days` being `30` will be created. As described in the [Logic Component](#logic-component) above, this will create a `RemindCommand` instance having the `RemindPredicate` instance as its field.
<div markdown="span" class="alert alert-info">:information_source: **Note:** If the number of days given is out of range, an error will be thrown and the application will re-prompt the user to input a valid value.</div>

Step 3. The `LogicManager` will call `RemindCommand#execute()` to start filtering the Persons list with the given `RemindPredicate`. Then, `Model#updateFilteredPersonList(Predicate<Person> p)` is called with the `RemindPredicate` as the input to perform the filtering of the list.

Step 4. Finally, a `CommandResult` instance will be created and returned to display filtered list of Persons to the user.

The following sequence diagram shows how the `remind` command works:

![RemindSequenceDiagram1](images/RemindSequenceDiagram1.png)
<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `RemindCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram. </div>

![RemindSequenceDiagram2](images/RemindSequenceDiagram2.png)

The following activity diagram summarises what happens when a user executes the command `remind 30`:

![RemindActivityDiagram](images/RemindActivityDiagram.png)

#### Design considerations

**Aspect: Whether `remind` command should take in a value:**
* Alternative 1: `remind` command without specifying the number of days, default 30 days (which is 1 month).
  * Pros: Shorter command for the user to use, simply just one word.
  * Cons: Not enough flexibility, user may want to find expiry dates beyond 30 days.
* Alternative 2 (current choice): `remind` command with number of days given by the user.
  * Pros: Allows more flexibility, now the user can find persons whose expiry dates is not only 30 days.
  * Cons: Need to determine the range of days allowed for the user to enter, security concerns such as integer overflow could occur if user decides to perform malicious activities.

### `sort`
The `sort` command allows the user to view the profiles arranged in order of earliest to latest policy expiration date, with those profiles that have no policy data placed at the end of the late

#### Implementation:
The sorted list will be displayed in the UI. The remind mechanism is facilitated by `Model` through the following operations:
* `Model#SortData()` - The Unique Person List of the Address Book is sorted using a PolicyExpiryDateComparator that implements Comparator<Person>

Given below is an example usage scenario and how the sort mechanism behaves at each step:

Step 1. The user wishes to see whose policy expires soon.

Step 2. The user executes ‘sort’ command to determine the persons whose insurance is going to finish the soonest. The ‘Main Window’ will call ‘LogicManager#execute(String s)’. The ‘LogicManager’ in turn calls ‘AddressBookParser#parseCommand(String s)’. Here the input is matched to ‘SortCommandParser’ and ‘ParseCommand#execute()’ is called.

Step 3. ‘SortCommandParser’ creates an instance of ‘SortCommand’ that is returned to ‘LogicManager#execute(String) s’. The method ‘CommandResult#execute()’ is then called where `Model#SortData()`is called **Note** If an additional argument is added following ‘sort’ command, for instance ‘sort 2’, no error will be thrown and the sorted list will be shown as normal. This is because the implementation ignores the arguments.

Step 4. Finally, a `CommandResult` instance will be created and returned to display sorted list of Persons to the user.

The following sequence diagram shows how the `sort` command works:

![SortSequenceDiagram1](images/SortSequenceDiagram1.png)

![SortSequenceDiagram2](images/SortSequenceDiagram2.png)

#### Design considerations

**Aspect: Whether ‘sort’ command should take in a value.**
* Alternative 1: (current choice) ‘sort’ command does not take in a value but does not an exception if an input is produced.
* Pros: Prevents the need for un necessary exceptions that might affect the running of the program
* Cons: The arguments might be nonsensical, for instance ‘sort 2’ could instead be used to provide the 2 most closely expiring profiles.
* Alternative 2 : `sort` command does not take in a value and produces exception if an input is produced
    * Pros: Prevents nonsensical inputs
    * Cons: Lack of functionality as specified for alternative 1. There is also a lack of flexibility.
* Alternative 3 : `sort` command takes in a value so the user can specify how many profiles they wish to view
    * Pros: Provides maximum flexibility and functionality

### `remark`
The `remark` command allows the user to add optional remarks / comments to a person.

#### Implementation:
`remark` works similarly to `edit`, but is only editing the `Remark` attribute of the `Person`. 
To add this feature, the following were done:
- Include optional `Remark` attribute in `Person` class
- Edit existing features to parse remarks (e.g. in `add` and `edit` to have a new CLI flag)

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a car insurance agent
* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Provide quick and easy contact management system of large car owners database with policies bought by them and have a better overview of the premium due dates.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​          | I want to …​                                                                                        | So that I can…​                                                                      |
|----------|------------------|-----------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| `* * *`  | beginner user    | add a person                                                                                        | expand my client list                                                                |
| `* * *`  | beginner user    | delete a person                                                                                     | remove entries who I am no longer working with                                       |
| `* * *`  | beginner user    | find person(s) by fields                                                                            | locate details of persons without having to go through the entire list               |
| `* * *`  | beginner user    | edit a person                                                                                       | make changes to details of a person                                                  |
| `* * *`  | expert user      | export the entire database                                                                          | have a secured copy of all my clients information                                    |
| `* *`    | first-time user  | see the layout of the application filled with dummy data                                            | learn how to use the application without corrupting my own data yet                  |
| `* *`    | first-time user  | delete all the dummy data                                                                           | start using the application for my own use                                           |
| `* *`    | first-time user  | import an existing database into the system                                                         | have all my contacts in one place for different insurance companies I am working for |
| `* *`    | beginner-user    | check which policies are close to completion                                                        | perform follow-up actions outside of the application                                 |
| `* *`    | experienced-user | have reminders for premium due dates expiry whenever I start the application                        | notify my clients about the upcoming payment                                         |
| `* *`    | experienced-user | add comments to a person                                                                            |                                                                                      |
| `* *`    | expert user      | sort persons by premium due date expiry                                                             | locate a person who needs to be urgently contacted                                   |
| `*`      | first-time user  | have a tutorial function                                                                            | familiarise myself with the features and understand how to use the application       |
| `*`      | experienced user | schedule follow-up calls or meetings with persons directly from the contact number in their profile | save the hassle of reaching out to them using another avenue                         |
| `*`      | experienced user | review and categorise persons as leads or clients                                                   | prioritise my outreach efforts                                                       |
| `*`      | experienced user | categorise persons by insurance company                                                             | easily find out which insurance company the person bought insurance from             |
| `*`      | experienced user | customise the layout of the application                                                             | better suit my preferences                                                           |
| `*`      | expert user      | generate reports on policy renewals and customer interactions                                       | assess my performance                                                                |
| `*`      | expert user      | differentiate between polices based on coverage types                                               | provide tailored advice to clients                                                   |
| `*`      | expert user      | track communication history with each person                                                        | provide personalised service                                                         |
| `*`      | expert user      | automatically edit a person who made their payment for the premium before the due date              | save the hassle of editing the person manually                                       |
| `*`      | expert user      | create custom commands                                                                              | successfully perform tasks in a shorter time                                         |

### Use cases

(For all use cases below, the **System** is the `InsureIQ` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC1 - List all persons**

**MSS**

1.  User requests to list all persons.
2.  InsureIQ shows a list of all persons.

    Use case ends.

**Use case: UC2 - Find persons**

**MSS**

1.  User requests to list persons based on a specific condition.
2.  InsureIQ shows a list of persons that satisfy the condition.

    Use case ends.

**Extensions**

* 2a. The conditions specified are of invalid format.

    * 2a1. InsureIQ shows an error message.

      Use case ends.

**Use case: UC3 - Add a person**

**MSS**

1.  User requests to add a person.
2.  InsureIQ shows a confirmation message that the person was added.

    Use case ends.

**Extensions**

* 2a. The adding of the person fails.

    * 2a1. InsureIQ shows an error message.

      Use case ends.

**Use case: UC4 - Delete a person**

**MSS**

1.  User requests to <u>list all persons (UC1)</u> or <u>find persons (UC2)</u>.
2.  InsureIQ shows a list of persons.
3.  User requests to delete a specific person in the list.
4.  InsureIQ shows a confirmation message that the person was deleted.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. InsureIQ shows an error message.

      Use case resumes at step 2.

**Use case: UC5 - Edit a person**

**MSS**

1.  User requests to <u>list all persons (UC1)</u> or <u>find persons (UC2)</u>.
2.  InsureIQ shows a list of persons.
3.  User requests to edit a specific person in the list.
4.  InsureIQ shows a confirmation message that the person was edited.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. InsureIQ shows an error message.

      Use case resumes at step 2.

* 3b. The fields to be edited are of invalid format.

    * 3b1. InsureIQ shows an error message.

      Use case resumes at step 2.

### Non-Functional Requirements

Should work on any _mainstream OS_ as long as it has Java `11` or above installed.

1. **Performance Requirements:**
    - The system should respond to user input within 1 second on average.
    - It should be able to handle a minimum of 100 concurrent user sessions without significant performance degradation.

2. **Scalability:**
    - The system should be scalable to accommodate a growing number of car insurance policies and customer records.
    - It should support at least 1,000 customer profiles and policies initially and be able to scale to 10,000 over time.

3. **Availability and Reliability:**
    - The system should be available 24/7, with planned maintenance windows communicated in advance.
    - It should have a reliability rate of at least 99.9%, minimizing downtime and data loss.

4. **Usability:**
    - A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
    - The CLI and GUI interfaces should be intuitive and user-friendly, allowing agents to perform tasks efficiently.
    - The system should provide clear error messages and support for keyboard shortcuts for CLI users.

5. **Maintainability:**
    - The codebase should follow industry best practices and be well-documented to facilitate maintenance and future updates.
    - Updates and bug fixes should be deployable without significant disruption to the system.

6. **Interoperability:**
    - The system should be compatible with various operating systems commonly used by insurance agents.
    - It should support importing/exporting data in _standard formats_ for interoperability with other systems.

7. **Documentation:**
   - Comprehensive documentation, including user manuals and technical guides, should be available to assist users and administrators.
   - The documentation should be regularly updated to reflect changes and improvements to the system.

8. **Response Time:**
   - The system should provide real-time updates on policy status and customer information, ensuring that agents can quickly access the information they need.
   - Queries for customer data should return results in less than 2 seconds.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **standard formats**: CSV, JSON

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
