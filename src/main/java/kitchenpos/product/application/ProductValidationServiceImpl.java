package kitchenpos.product.application;

import java.util.List;
import java.util.Optional;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductValidationServiceImpl implements ProductValidationService {

    private final ProductRepository productRepository;

    public ProductValidationServiceImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Price> calculateAmountSum(final List<Long> productIds) {
        final List<Product> products = productRepository.findAllById(productIds);
        return products.stream()
                .map(Product::getPrice)
                .reduce(Price::add);
    }

    @Override
    public boolean existsProductsByIdIn(final List<Long> productIds) {
       return productRepository.existsAllById(productIds);
    }
}
