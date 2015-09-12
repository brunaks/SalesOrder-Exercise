package UseCases.Product;

import Entities.Product.ProductInfo;
import Interfaces.Receivers.ProductReceiver;
import Interfaces.Persistence.ProductRepository;

/**
 * Created by I848075 on 20/08/2015.
 */
public class ReadProductUseCase {
    private ProductRepository repository;
    private ProductReceiver productReceiver;

    public ReadProductUseCase(ProductRepository repository, ProductReceiver productReceiver) {
        this.repository = repository;
        this.productReceiver = productReceiver;
    }

    public ProductInfo getProductInfoByProductName(String productName) {
        ProductInfo productInfo = this.repository.getProductInfoByName(productName);
        if (productInfo != null)
            return productInfo;
        else
            return null;
    }

    public ProductInfo getProductInfoById(String id) {
        ProductInfo productInfo = this.repository.getProductInfoById(id);
        if (productInfo != null)
            return productInfo;
        else
            return null;
    }
}
