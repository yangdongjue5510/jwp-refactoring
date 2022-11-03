package kitchenpos.support.fixture;

import kitchenpos.table.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createDefaultWithoutId() {
        return new OrderTable( 0, true);
    }

    public static OrderTable create(final boolean isEmpty, final Integer guestCount) {
        return new OrderTable(guestCount, isEmpty);
    }
}