package seedu.address.model.policy;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a car insurance policy issue / expiry date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPolicyDate(String)}
 */
public class PolicyDate {

    public static final String MESSAGE_CONSTRAINTS = "Policy issue / expiry date should be in the format dd-mm-yyyy";

    /*
     * Should be in the format dd-mm-yyyy, checked with SimpleDateFormat
     */
    public static final String VALIDATION_DATE_FORMAT = "dd-MM-yyyy";
    public static final String DEFAULT_VALUE = "01-01-1000";

    public final LocalDate date;

    /**
     * Constructs a {@code PolicyDate}.
     *
     * @param policyDate A valid policy expiry date.
     */
    public PolicyDate(String policyDate) {
        requireNonNull(policyDate);
        checkArgument(isValidPolicyDate(policyDate), MESSAGE_CONSTRAINTS);
        date = LocalDate.parse(policyDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    /**
     * Returns true if a given string is a valid date in the format dd-mm-yyyy.
     */
    public static boolean isValidPolicyDate(String test) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(VALIDATION_DATE_FORMAT);
            format.parse(test);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PolicyDate)) {
            return false;
        }

        PolicyDate otherPolicyDate = (PolicyDate) other;
        return date.equals(otherPolicyDate.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

}
