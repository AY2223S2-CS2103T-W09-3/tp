package seedu.careflow.ui;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.careflow.logic.CareFlowLogic;
import seedu.careflow.model.patient.Patient;

/**
 * Panel containing the list of patients.
 */
public class PatientListPanel extends UiPart<Region> {
    private static final String FXML = "PatientListPanel.fxml";
    @FXML
    private ListView<Patient> patientListView;

    @FXML
    private Label selectedName;
    @FXML
    private Label selectedPhone;
    @FXML
    private Label selectedAddress;
    @FXML
    private Label selectedEmail;
    @FXML
    private Label selectedBirthDate;
    @FXML
    private Label selectedGender;
    @FXML
    private Label selectedIc;
    @FXML
    private Label selectedDrugAllergy;
    @FXML
    private Label selectedEmergencyContact;

    /**
     * Creates a {@code PatientListPanel} with the given {@code ObservableList}.
     */
    public PatientListPanel(ObservableList<Patient> patientList, CareFlowLogic logic) {
        super(FXML);
        // PERSON LIST
        patientListView.setItems(patientList);
        patientListView.setCellFactory(listView -> new PatientListViewCell());
        displayPatientDetail();
        logic.getFilteredPatientList().addListener(new ListChangeListener<Patient>() {
            @Override
            public void onChanged(Change<? extends Patient> c) {
                Patient selectedPatient = patientListView.getSelectionModel().getSelectedItem();
                setPatientDetailDisplay(selectedPatient);
            }
        });
    }

    private void displayPatientDetail() {
        patientListView.setOnMouseClicked(event -> {
            Patient selectedPatient = patientListView.getSelectionModel().getSelectedItem();
            setPatientDetailDisplay(selectedPatient);
        });
    }

    private void setPatientDetailDisplay(Patient selectedPatient) {
        selectedName.setText(selectedPatient.getName().fullName);
        selectedPhone.setText("Tel: " + selectedPatient.getPhone().value);
        selectedAddress.setText("Address: " + selectedPatient.getAddress().value);
        selectedEmail.setText("Email: " + selectedPatient.getEmail().value);
        selectedBirthDate.setText("Date of Birth: " + selectedPatient.getBirthDate().value);
        selectedGender.setText("Gender: " + selectedPatient.getGender().value);
        selectedIc.setText("Ic: " + selectedPatient.getIc().value);
        if (selectedPatient.getDrugAllergy() == null) {
            selectedDrugAllergy.setText("Drug Allergy: - ");
        } else {
            selectedDrugAllergy.setText("Drug Allergy: " + selectedPatient.getDrugAllergy().value);
        }
        if (selectedPatient.getEmergencyContact() == null) {
            selectedEmergencyContact.setText("Emergency Contact: - ");
        } else {
            selectedEmergencyContact.setText("Emergency Contact: " + selectedPatient.getEmergencyContact().value);
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Patient} using a {@code PatientCard}.
     */
    class PatientListViewCell extends ListCell<Patient> {
        @Override
        protected void updateItem(Patient patient, boolean empty) {
            super.updateItem(patient, empty);

            if (empty || patient == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PatientCard(patient, getIndex() + 1).getRoot());
            }
        }
    }
}
