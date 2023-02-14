package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Represent the remark of a Person in the address book
 */
public class Remark {

    public final String value;

    /**
     * Constructs a {@code remark}
     * @param remark A valid remark
     */
    public Remark(String remark) {
        requireNonNull(remark);
        this.value = remark;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Remark)) {
            return false;
        }
        Remark r = (Remark) other;
        return r.value.equals(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
