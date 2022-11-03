package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final MenuCreateRequest menuCreateRequest) {
        final List<MenuProduct> menuProducts = mapAllToMenuProducts(menuCreateRequest);
        final MenuGroup menuGroup = getMenuGroup(menuCreateRequest);
        final Price price = new Price(menuCreateRequest.getPrice());
        final Menu menu = new Menu(menuCreateRequest.getName(), price, menuGroup.getId(), menuProducts);
        menuValidator.validate(menu);
        return menuRepository.save(menu);
    }

    private MenuGroup getMenuGroup(final MenuCreateRequest menuCreateRequest) {
        return menuGroupRepository.findById(menuCreateRequest.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);
    }

    private List<MenuProduct> mapAllToMenuProducts(final MenuCreateRequest menuCreateRequest) {
        return menuCreateRequest.getMenuProducts().stream()
                .map(this::mapToMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct mapToMenuProduct(final MenuProductCreateRequest request) {
        return new MenuProduct(request.getProductId(), request.getQuantity(), null);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}