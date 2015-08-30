/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public class UpdateProduct {

    private ProductReceiver receiver;
    private ProductRepository repository;
    private String productId;
    private ProductInfo newProductInfo;

    public UpdateProduct(ProductRepository repository, ProductReceiver receiver) {
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
