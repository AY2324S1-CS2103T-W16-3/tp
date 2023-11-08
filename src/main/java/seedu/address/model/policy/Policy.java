package seedu.address.model.policy;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a person's policy in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Policy {
    private final Company company;
    private final PolicyNumber policyNumber;
    private final PolicyDate policyIssueDate;
    private final PolicyDate policyExpiryDate;

    /**
     * Every field must be present and not null.
     * In the case of leads with null policy fields, default values will be put in place by the respective classes.
     */
    public Policy(Company company, PolicyNumber policyNumber, PolicyDate policyIssueDate, PolicyDate policyExpiryDate) {
        this.company = company;
        this.policyNumber = policyNumber;
        this.policyIssueDate = policyIssueDate;
        this.policyExpiryDate = policyExpiryDate;
    }

    public Company getCompany() {
        return company;
    }
    public PolicyNumber getPolicyNumber() {
        return policyNumber;
    }

    public PolicyDate getPolicyIssueDate() {
        return policyIssueDate;
    }

    public PolicyDate getPolicyExpiryDate() {
        return policyExpiryDate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Policy)) {
            return false;
        }

        Policy otherPolicy = (Policy) other;
        return company.equals(otherPolicy.company)
                && policyNumber.equals(otherPolicy.policyNumber)
                && policyIssueDate.equals(otherPolicy.policyIssueDate)
                && policyExpiryDate.equals(otherPolicy.policyExpiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(company, policyNumber, policyIssueDate, policyExpiryDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("company", company)
                .add("policy number", policyNumber)
                .add("policy issue date", policyIssueDate)
                .add("policy expiry date", policyExpiryDate)
                .toString();
    }

    /**
     * Compares the policy's issue date and expiry date
     * Returns more than 1 if the expiry date is after issue date
     * Returns less than 1 otherwise
     * @return int
     */
    public int compareDates() {
        return this.getPolicyExpiryDate().compareTo(this.getPolicyIssueDate());
    }

    /**
     * Return a string representation of the Policy that will be displayed on the Person's Card or the successful
     * command box.
     *
     * @param isPersonCard true if the display is for the Person card, false otherwise.
     * @return the string representation of the Policy.
     */
    public String toDisplay(boolean isPersonCard) {
        if (policyNumber.toString().equals(PolicyNumber.DEFAULT_VALUE)) {
            // Policy does not actually exist
            return "No Policy Found";
        }

        if (isPersonCard) {
            return "Company: " + company + "\n" + "Policy Number: " + policyNumber + "\n"
                    + "Policy Issue Date: " + policyIssueDate + "\n" + "Policy Expiry Date: " + policyExpiryDate;
        }
        return company + ", " + policyNumber + ", " + policyIssueDate + ", " + policyExpiryDate;
    }
}
