package seedu.careflow.logic.commands.drugcommands;

import static seedu.careflow.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.careflow.logic.commands.CommandTestUtil.assertDrugCommandFailure;
import static seedu.careflow.testutil.TypicalDrugs.getTypicalDrugInventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.careflow.model.CareFlowModel;
import seedu.careflow.model.CareFlowModelManager;
import seedu.careflow.model.HospitalRecord;
import seedu.careflow.model.PatientRecord;
import seedu.careflow.model.UserPrefs;
import seedu.careflow.model.drug.Drug;
import seedu.careflow.testutil.DrugBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {
    private CareFlowModel model;

    @BeforeEach
    public void setUp() {
        model = new CareFlowModelManager(new PatientRecord(), getTypicalDrugInventory(),
                new HospitalRecord(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Drug validDrug = new DrugBuilder().build();

        CareFlowModel expectedModel = new CareFlowModelManager(new PatientRecord(), model.getDrugInventory(),
                new HospitalRecord(), new UserPrefs());
        expectedModel.addDrug(validDrug);

        assertCommandSuccess(new AddCommand(validDrug), model,
                String.format(AddCommand.MESSAGE_SUCCESS, validDrug), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Drug drugInList = model.getDrugInventory().getDrugList().get(0);
        assertDrugCommandFailure(new AddCommand(drugInList), model, AddCommand.MESSAGE_DUPLICATE_DRUG);
    }
}


