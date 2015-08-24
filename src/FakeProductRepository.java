import java.util.ArrayList;

/**
 * Created by I848075 on 20/08/2015.
 */
public class FakeProductRepository implements ProductRepository{
    private ProductReceiver receiver;
    private ArrayList<Product> productsSaved = new ArrayList<Product>();

    public FakeProductRepository(ProductReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void saveProduct(Product product) {
        Product productToBeSaved = this.createProduct(product);
        this.productsSaved.add(productToBeSaved);
        this.receiver.productWasSaved();
    }

    private Product createProduct(Product product) {
        Product productToBeSaved = new Product();
        productToBeSaved.setName(product.getName());
        productToBeSaved.setDescription(product.getDescription());
        productToBeSaved.setPrice(product.getPrice());
        productToBeSaved.setUnitsInStock(product.getUnitsInStock());
        return productToBeSaved;
    }

    @Override
    public Product getProductByName(String productName) {
        for (int i = 0; i < this.productsSaved.size(); i++) {
            if (this.productsSaved.get(i).getName().equalsIgnoreCase(productName)) {
                this.receiver.productFound();
                return this.productsSaved.get(i);
            }
        }
        this.receiver.productWasNotFound();
        return null;
    }
}
