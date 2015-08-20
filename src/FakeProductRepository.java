/**
 * Created by I848075 on 20/08/2015.
 */
public class FakeProductRepository implements ProductRepository{
    private ProductReceiver receiver;
    private Product product;

    public FakeProductRepository(ProductReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void saveProduct(Product product) {
        this.product = product;
        this.receiver.productWasSaved();
    }

    @Override
    public Product getProductByName(String productName) {
        if (this.product != null && this.product.getName().equalsIgnoreCase(productName)) {
            this.receiver.productFound();
            return this.product;
        }   else {
            this.receiver.productWasNotFound();
            return null;
        }
    }
}
