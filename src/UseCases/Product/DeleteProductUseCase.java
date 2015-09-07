package UseCases.Product;

import Interfaces.Receivers.ProductReceiver;
import Interfaces.Persistence.ProductRepository;

/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public class DeleteProductUseCase {
    private ProductRepository repository;
    private ProductReceiver receiver;

    public DeleteProductUseCase(ProductRepository repository, ProductReceiver receiver) {
        this.repository = repository;
        this.receiver = receiver;
    }

    public void executeWithId(String productId) {
        if (repository.getProductById(productId) != null) {
            repository.deleteProductWithId(productId);
        } else {
            receiver.deleteFailed();
        }
    }
}
