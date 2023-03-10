package seedu.address.logic.commands.drugcommands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.CareFlowModel;

/**
 * Lists all drugs in the drug inventory
 */
public class ListCommand extends Command {
    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_SUCCESS = "Listed all drugs";

    @Override
    public CommandResult execute(CareFlowModel model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredDrugList(model.PREDICATE_SHOW_ALL_DRUGS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
