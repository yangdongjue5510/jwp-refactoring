package kitchenpos.product.application;

import java.util.List;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest productCreateRequest) {
        final Product product = productCreateRequest.toProduct();
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}