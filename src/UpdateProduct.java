/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public class UpdateProduct {

    private ProductReceiver receiver;
    private ProductRepository repository;
    private String productId;

    public UpdateProduct(ProductRepository repository, ProductReceiver receiver) {
        this.repository = repository;
        this.receiver = receiver;
    }

    public void withId(String productId) {
        this.productId = productId;
    }

    public void setNewProductInfo(ProductInfo productInfo) {

    }
}
