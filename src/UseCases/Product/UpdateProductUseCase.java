package UseCases.Product;

import Entities.Product.ProductInfo;
import Interfaces.Receivers.ProductReceiver;
import Interfaces.Persistence.ProductRepository;

/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public class UpdateProductUseCase {

    private ProductReceiver receiver;
    private ProductRepository repository;
    private String productId;
    private ProductInfo newProductInfo;

    public UpdateProductUseCase(ProductRepository repository, ProductReceiver receiver) {
        this.repository = repository;
        this.receiver = receiver;
    }

    public void withId(String productId) {
        this.productId = productId;
    }

    public void setNewProductInfo(ProductInfo newProductInfo) {
        this.newProductInfo = newProductInfo;
        if (productExists() && newProductInfo.isValid()) {
            setNewInfo();
        } else {
            receiver.updateFailed();
        }
    }

    private void setNewInfo() {
        repository.updateProduct(this.productId, this.newProductInfo);
    }

    private boolean productExists() {
        return repository.getProductById(this.productId) != null;
    }

}
