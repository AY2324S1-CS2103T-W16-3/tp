package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_MONTH;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.PredicateUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.month.DeleteMonth;
import seedu.address.model.person.CompanyContainsKeywordsPredicate;
import seedu.address.model.person.HasPolicyPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PolicyExpiryInDeleteMonthPredicate;
import seedu.address.model.policy.Company;

/**
 * Deletes all people who policy expiry date is in the specific month.
 */
public class BatchDeleteCommand extends Command {

    public static final String COMMAND_WORD = "batchdelete";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": batch delete people whose policy: \n"
            + "(i) expiry date is in the corresponding month and year OR \n"
            + "(ii) belongs to a company.\n"
            + "(i) Parameter: "
            + PREFIX_DELETE_MONTH + "MM-yyyy\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DELETE_MONTH + "11-2022\n"
            + "(ii) Parameter: "
            + PREFIX_COMPANY + "COMPANY NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_COMPANY + "Allianz";

    public static final String MESSAGE_DELETE_PEOPLE_SUCCESS = "Batch delete people whose policy %1$s";

    private DeleteMonth month = null;
    private Company company = null;

    /**
     * Creates an BatchDeleteCommand to batch delete the specified {@code Person}
     * based on policy expiry date
     */
    public BatchDeleteCommand(DeleteMonth deleteMonth) {
        requireNonNull(deleteMonth);
        this.month = deleteMonth;
    }

    /**
     * Creates an BatchDeleteCommand to batch delete the specified {@code Person}
     * based on policy company
     */
    public BatchDeleteCommand(Company company) {
        requireNonNull(company);
        this.company = company;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Predicate<Person> p;
        String resultInfo;

        if (month != null) {
            p = new PolicyExpiryInDeleteMonthPredicate(month);
            resultInfo = "expiry date is in" + month.toString();
        } else if (company != null) {
            p = new CompanyContainsKeywordsPredicate(company.toString());
            resultInfo = "company contains keyword: " + company.toString();
        } else {
            throw new CommandException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    BatchDeleteCommand.MESSAGE_USAGE));
        }

        model.batchDeleteWithPredicate(addHasPolicyPredicate(p));

        return new CommandResult(String.format(MESSAGE_DELETE_PEOPLE_SUCCESS, resultInfo));
    }

    /**
     * Combines given predicate with predicate that checks a person has policy.
     *
     * @param p A predicate used to test a person.
     * @return A predicate which is combination of given predicate and predicate that checks a person has policy.
     */
    private Predicate<Person> addHasPolicyPredicate(Predicate<Person> p) {
        List<Predicate<Person>> predicates = new ArrayList<>();

        predicates.add(new HasPolicyPredicate());
        predicates.add(p);

        Predicate<Person> newPredicate = PredicateUtil.combinePredicates(predicates);

        return newPredicate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BatchDeleteCommand)) {
            return false;
        }

        BatchDeleteCommand otherBatchDeleteCommand = (BatchDeleteCommand) other;
        return month.equals(otherBatchDeleteCommand.month);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toDelete", month)
                .toString();
    }
}
