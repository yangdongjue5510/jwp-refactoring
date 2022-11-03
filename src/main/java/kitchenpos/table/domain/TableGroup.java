package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.InvalidOrderTableToGroupException;
import kitchenpos.exception.NotEnoughOrderTablesSizeException;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "table_group")
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    private TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        orderTables.forEach(TableGroup::validateOrderTableIsNotEmptyOrTableGroupNotNull);
        orderTables.forEach(orderTable -> orderTable.setEmpty(false));
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null , createdDate, orderTables);
    }

    private static void validateOrderTableIsNotEmptyOrTableGroupNotNull(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new InvalidOrderTableToGroupException();
        }
    }

    private static void validateOrderTableSize(final List<OrderTable> orderTables) {
        final int size = orderTables.size();
        if (CollectionUtils.isEmpty(orderTables) || size < 2) {
            throw new NotEnoughOrderTablesSizeException(size);
        }
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::releaseGroup);
    }

    public void addOrderTable(final OrderTable orderTable) {
        final List<OrderTable> orderTables = new ArrayList<>(this.orderTables);
        orderTables.add(orderTable);
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}