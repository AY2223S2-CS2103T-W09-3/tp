package seedu.careflow.logic.commands.patientcommands;

import static java.util.Objects.requireNonNull;
import static seedu.careflow.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.careflow.logic.parser.CliSyntax.PREFIX_DOB;
import static seedu.careflow.logic.parser.CliSyntax.PREFIX_DRUG_ALLERGY;
import static seedu.careflow.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.careflow.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT_NUMBER;
import static seedu.careflow.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.careflow.logic.parser.CliSyntax.PREFIX_IC;
import static seedu.careflow.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.careflow.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.careflow.model.CareFlowModel.PREDICATE_SHOW_ALL_PATIENTS;

import java.util.List;
import java.util.Optional;

import seedu.careflow.commons.util.CollectionUtil;
import seedu.careflow.logic.commands.Command;
import seedu.careflow.logic.commands.CommandResult;
import seedu.careflow.logic.commands.exceptions.CommandException;
import seedu.careflow.model.CareFlowModel;
import seedu.careflow.model.person.Address;
import seedu.careflow.model.person.DateOfBirth;
import seedu.careflow.model.person.DrugAllergy;
import seedu.careflow.model.person.Email;
import seedu.careflow.model.person.Gender;
import seedu.careflow.model.person.Ic;
import seedu.careflow.model.person.Name;
import seedu.careflow.model.person.Patient;
import seedu.careflow.model.person.Phone;

/**
 * Update the details of an existing person in the careflow.
 */
public class UpdateCommand extends Command {

    public static final String COMMAND_WORD = "update";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Update the details of the person identified given the patient name. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: Patient name"
            + " [ " + PREFIX_PHONE + " PHONE ] "
            + " [ " + PREFIX_EMAIL + " EMAIL ] "
            + " [ " + PREFIX_ADDRESS + " ADDRESS ] "
            + " [ " + PREFIX_DOB + " DATE_OF_BIRTH ] "
            + " [ " + PREFIX_GENDER + " GENDER ] "
            + " [ " + PREFIX_IC + " IC ] "
            + " [ " + PREFIX_NAME + " DRUG ALLERGY NUMBER ] "
            + " [ " + PREFIX_EMERGENCY_CONTACT_NUMBER + " EMRGENCY CONTACT ]\n"
            + "Example: " + COMMAND_WORD + " John "
            + PREFIX_PHONE + " 91234567 "
            + PREFIX_EMAIL + " johndoe@example.com "
            + PREFIX_DRUG_ALLERGY + " Aspirin "
            + PREFIX_EMERGENCY_CONTACT_NUMBER + " 93746552";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Updated Patient: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Name name;
    private final EditPatientDescriptor editPatientDescriptor;

    /**
     * @param name of the person in the filtered person list to edit
     * @param editPatientDescriptor details to edit the person with
     */
    public UpdateCommand(Name name, EditPatientDescriptor editPatientDescriptor) {
        requireNonNull(name);
        requireNonNull(editPatientDescriptor);

        this.name = name;
        this.editPatientDescriptor = new EditPatientDescriptor(editPatientDescriptor);
    }

    @Override
    public CommandResult execute(CareFlowModel careFlowModel) throws CommandException {
        requireNonNull(careFlowModel);
        List<Patient> lastShownList = careFlowModel.getFilteredPatientList();

        Patient patientToEdit = null;
        for (Patient patient: lastShownList) {
            if (patient.getName().equals(name)) {
                patientToEdit = patient;
                break;
            }
        }
        Patient editedPatient = createEditedPatient(requireNonNull(patientToEdit), editPatientDescriptor);

        if (!patientToEdit.isSamePatient(editedPatient) && careFlowModel.hasPatient(editedPatient)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        careFlowModel.setPatient(patientToEdit, editedPatient);
        careFlowModel.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPatient));
    }

    /**
     * Creates and returns a {@code Patient} with the details of {@code patientToEdit}
     * edited with {@code editPatientDescriptor}.
     */
    private static Patient createEditedPatient(Patient patientToEdit, EditPatientDescriptor editPatientDescriptor) {
        assert patientToEdit != null;

        Name updatedName = editPatientDescriptor.getName().orElse(patientToEdit.getName());
        Phone updatedPhone = editPatientDescriptor.getPhone().orElse(patientToEdit.getPhone());
        Email updatedEmail = editPatientDescriptor.getEmail().orElse(patientToEdit.getEmail());
        Address updatedAddress = editPatientDescriptor.getAddress().orElse(patientToEdit.getAddress());
        DateOfBirth updatedBirthDate = editPatientDescriptor.getDateOfBirth().orElse(patientToEdit.getBirthDate());
        Gender updatedGender = editPatientDescriptor.getGender().orElse(patientToEdit.getGender());
        Ic updatedIc = editPatientDescriptor.getIc().orElse(patientToEdit.getIc());
        DrugAllergy updatedDrugAllergy = editPatientDescriptor
                .getDrugAllergy()
                .orElse(patientToEdit.getDrugAllergy());
        Phone updatedEmergencyContact = editPatientDescriptor
                .getEmergencyContact()
                .orElse(patientToEdit.getEmergencyContact());

        return new Patient(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedBirthDate, updatedGender,
                updatedIc, updatedDrugAllergy, updatedEmergencyContact);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UpdateCommand)) {
            return false;
        }

        // state check
        UpdateCommand e = (UpdateCommand) other;
        return name.equals(e.name)
                && editPatientDescriptor.equals(e.editPatientDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPatientDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private DateOfBirth dateOfBirth;
        private Gender gender;
        private Ic ic;
        private DrugAllergy drugAllergy;
        private Phone emergencyContact;

        public EditPatientDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPatientDescriptor(EditPatientDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setDateOfBirth(toCopy.dateOfBirth);
            setGender(toCopy.gender);
            setIc(toCopy.ic);
            setDrugAllergy(toCopy.drugAllergy);
            setEmergencyContact(toCopy.emergencyContact);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address,
                    dateOfBirth, gender, ic, drugAllergy, emergencyContact);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setDateOfBirth(DateOfBirth dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public Optional<DateOfBirth> getDateOfBirth() {
            return Optional.ofNullable(dateOfBirth);
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public Optional<Gender> getGender() {
            return Optional.ofNullable(gender);
        }

        public void setIc(Ic ic) {
            this.ic = ic;
        }

        public Optional<Ic> getIc() {
            return Optional.ofNullable(ic);
        }

        public void setDrugAllergy(DrugAllergy drugAllergy) {
            this.drugAllergy = drugAllergy;
        }

        public Optional<DrugAllergy> getDrugAllergy() {
            return Optional.ofNullable(drugAllergy);
        }

        public void setEmergencyContact(Phone emergencyContact) {
            this.emergencyContact = emergencyContact;
        }

        public Optional<Phone> getEmergencyContact() {
            return Optional.ofNullable(emergencyContact);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPatientDescriptor)) {
                return false;
            }

            // state check
            EditPatientDescriptor e = (EditPatientDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getDateOfBirth().equals(e.getDateOfBirth())
                    && getGender().equals(e.getGender())
                    && getIc().equals(e.getIc())
                    && getDrugAllergy().equals(e.getDrugAllergy())
                    && getEmergencyContact().equals(e.getEmergencyContact());
        }
    }
}
