/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public class DeleteProduct {
    private ProductRepository repository;
    private ProductReceiver receiver;

    public DeleteProduct(ProductRepository repository, ProductReceiver receiver) {
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
