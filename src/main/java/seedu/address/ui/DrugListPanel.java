package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.drug.Drug;
import seedu.address.model.person.Patient;

public class DrugListPanel extends UiPart<Region> {
    private static final String FXML = "DrugListPanel.fxml";

    @FXML
    private ListView<Drug> drugListView;

    @FXML
    private Label selectedTradeName;

    @FXML
    private Label selectedActiveIngredient;

    @FXML
    private Label selectedDirection;

    @FXML
    private Label selectedPurposes;

    @FXML
    private Label selectedSideEffects;

    @FXML
    private Label selectedStorageCount;

    public DrugListPanel(ObservableList<Drug> drugList) {
        super(FXML);
        // DRUG LIST
        drugListView.setItems(drugList);
        drugListView.setCellFactory(listView -> new DrugListViewCell());
        displayDrugDetail();
    }

    private void displayDrugDetail() {
        drugListView.setOnMouseClicked(event -> {
            Drug selectedDrug = drugListView.getSelectionModel().getSelectedItem();
            System.out.println(selectedDrug.toString());
            selectedTradeName.setText(selectedDrug.getTradeName().tradeName);
            selectedActiveIngredient.setText("Active Ingredient: " + selectedDrug.getActiveIngredient().value);
            selectedDirection.setText("Direction: " + selectedDrug.getDirection().value);
            selectedPurposes.setText("Purposes: " + selectedDrug.getPurposes().purpose);
            selectedSideEffects.setText("Side Effects: " + selectedDrug.getSideEffects().sideEffect);
            selectedStorageCount.setText("Storage Count: " + selectedDrug.getStorageCount().toString());
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Drug} using a {@code DrugCard}.
     */
    class DrugListViewCell extends ListCell<Drug> {
        @Override
        protected void updateItem(Drug drug, boolean empty) {
            super.updateItem(drug, empty);

            if (empty || drug == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new DrugCard(drug, getIndex() + 1).getRoot());
            }
        }
    }

}
