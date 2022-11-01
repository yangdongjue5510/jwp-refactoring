package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class MenuFixture {

    public static Menu createWithPrice(final Long menuGroupId, final Long price, final Product... product) {
        return createWithIdAndPrice( menuGroupId, price, product);
    }

    private static Menu createWithIdAndPrice(final Long menuGroupId, final Long price,
                                            final Product... product) {
        final List<MenuProduct> menuProducts = convertToEmptyMenuIdMenuProduct(product);
        if (price == null) {
            return new Menu("name", new Price(price), menuGroupId, menuProducts);
        }
        final BigDecimal priceValue = BigDecimal.valueOf(price);
        return new Menu("name", new Price(priceValue), menuGroupId, menuProducts);
    }

    private static List<MenuProduct> convertToEmptyMenuIdMenuProduct(final Product... product) {
        return Arrays.stream(product)
                .map(it -> MenuProductFixture.createDefaultWithoutId(it, null))
                .collect(Collectors.toList());
    }
}
