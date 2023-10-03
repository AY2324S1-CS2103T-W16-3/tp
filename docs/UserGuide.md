---
layout: page
title: User Guide
---

InsureIQ is a **contact management system of large car owners database with policies bought by them** optimized for use via a Command Line Interface (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, InsureIQ can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

1. Download the latest `addressbook.jar` from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/Mary i/627A c/73052859 l/SLU5237J` : Adds a contact named `Mary` to the InsureIQ.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaning how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to the database.

Format: `add n/NAME i/NRIC c/CONTACT NUMBER l/LICENCE PLATE…​`

* Add a person's details in the database.
* **All** of the fields must be present when using this command.

Examples:
* `add n/Mary i/627A c/73052859 l/SLU5237J` creates a person whose name is `Mary`, NRIC last four digits is `627A`, contact number is `73052859`, licence plate is `SLU5237J` in the database.

Acceptable values for each parameter:
* `n/NAME`: Alphabets
* `i/NRIC`: Alphanumeric, _exactly_ 4 characters
* `c/CONTACT NUMBER`: Numeric, _exactly_ 8 characters
* `l/LICENCE PLATE`: Alphanumeric, _up to_ 9 characters

Expected output upon success: [coming soon]

Expected output upon failure:
* Any one of the fields missing: `Error: Please provide all required fields (pn, new, field)`
* Fields specified for add are not valid or empty: `Error: Invalid field for adding.`
* Format of added value is incorrect or not allowed for the specified field: `Error: Invalid value format.`


### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Editing a person : `edit`

Modify and/or updates existing policy information in the database.

Format: `edit INDEX [l/LICENCEPLATE] [pn/POLICY NUMBER] [pi/POLICY ISSUE
DATE] [pe/POLICY EXPIRY DATE]…​`

* Edits the person policy details at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* **At least one** of the optional fields must be provided.
* Existing values will be updated to the input values.


Examples:
*  `edit 1 l/SNB9876E` updates the policy at the INDEX number 1 with the new license number provided.
*  `edit 2 pn/AB12345J pe/31-12-2024` updates the policy at the INDEX number 2 with the new policy number and expiration date.

Acceptable values for each parameter:
* `l/LICENCEPLATE`: Alphanumeric, _up to_ 9 characters.
* `pn/POLICY NUMBER`: Alphanumeric, _exactly_ 8 characters.
* `pi/POLICY ISSUE DATE` and `pe/POLICY EXPIRY DATE`: Date in the format dd-mm-yyyy.

Expected output upon success: [coming soon]

Expected output upon failure:
* Policy number does not exist: `Error: Policy number not found.`
* Field specified for update is not valid or empty: `Error: Invalid field for updating.`
* New value format is incorrect or not allowed for the specified field: `Error: Invalid new value format.`
* Any of the mandatory fields are missing: `Error: Please provide all required fields.`



### Locating persons by fields: `find`

Finds person(s) whose fields matches any of the given fields.

Format: `find [n/NAME] [i/NRIC] [c/CONTACT NUMBER] [l/LICENCE PLATE]
[pn/POLICY NUMBER] [pi/POLICY ISSUE DATE] [pe/POLICY EXPIRY DATE]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the fields does not matter. e.g. `Hans Bo` will match `Bo Hans`
* **At least one** of the fields must be present
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one field will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find n/mary` returns all profiles that has the name `Mary`
* `find n/john pn/AB12345J` returns the profile whose name contains `John` with policy number `AB12345J`

Acceptable values for each parameter:
* `n/NAME`: Alphabets 
* `i/NRIC`: Alphanumeric, _exactly_ 4 characters 
* `c/CONTACT NUMBER`: Numeric, _exactly_ 8 characters 
* `l/LICENCE PLATE`: Alphanumeric, _up to_ 9 characters 
* `pn/POLICY NUMBER`: Alphanumeric, _exactly_ 8 characters 
* `pi/POLICY ISSUE DATE` and `pe/POLICY EXPIRY DATE`: Date in the format _dd-mm-yyyy_

Expected output upon success: [coming soon]

Expected output upon failure:
* Format error in any field:`Error: Please adhere to the format for the fields`
* No field given: `Error: Please give at least one field`
* Field flag given but no value: `Error: Please give a value in the field(s) indicated`


### Deleting a person : `delete`

Deletes the specified person from the database.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed list of car owners and is a compulsory field.

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the database.
* `find n/Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

Acceptable values for each parameter:
* `INDEX` : It must be a **must be a positive integer** 1, 2, 3, …​

Expected output upon success : [coming soon]

Expected output upon failure:
* Profile at the specified `INDEX` cannot be found: `Error: Invalid Index`
* Missing `INDEX` parameter: `Error: Missing Index`
* Incorrect `INDEX` parameter: `Error: The parameter is not of the type positive integer`

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action     | Format, Examples                                                                                                                                                     |
|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add**    | `add n/NAME i/NRIC c/CONTACT NUMBER l/LICENCE PLATE…​` <br> e.g., `add n/Mary i/627A c/73052859 l/SLU5237J`                                                          |
| **Clear**  | `clear`                                                                                                                                                              |
| **Delete** | `delete INDEX`<br> e.g., `delete 3`                                                                                                                                  |
| **Edit**   | `edit INDEX [l/LICENCEPLATE] [pn/POLICY NUMBER] [pi/POLICY ISSUE DATE] [pe/POLICY EXPIRY DATE]…​`<br> e.g.,`edit 2 pn/AB12345J pe/31-12-2024`                        |
| **Find**   | `find [n/NAME] [i/NRIC] [c/CONTACT NUMBER] [l/LICENCE PLATE] [pn/POLICY NUMBER] [pi/POLICY ISSUE DATE] [pe/POLICY EXPIRY DATE]`<br> e.g., `find n/John /pn AB12345J` |
| **List**   | `list`                                                                                                                                                               |
| **Help**   | `help`                                                                                                                                                               |
