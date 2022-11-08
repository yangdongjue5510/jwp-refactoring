package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidPriceException;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        validateNullOrNegative(value);
        this.value = value;
    }

    public Price(final Long value) {
        this(mapToBigDecimal(value));
    }

    private static BigDecimal mapToBigDecimal(final Long value) {
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf(value);
    }

    private static void validateNullOrNegative(final BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
        }
    }

    public Price add(final Price consequent) {
        return new Price(this.value.add(consequent.value));
    }

    public boolean isExpansiveThan(final Price consequent) {
        return this.value.compareTo(consequent.getValue()) > 0;
    }

    public Price multiply(final long consequent) {
        final BigDecimal BigDecimalConsequent = BigDecimal.valueOf(consequent);
        return new Price(value.multiply(BigDecimalConsequent));
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }
        final Price price = (Price) o;
        return price.value.compareTo(value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}