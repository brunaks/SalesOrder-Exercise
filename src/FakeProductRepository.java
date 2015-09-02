import java.util.ArrayList;

/**
 * Created by I848075 on 20/08/2015.
 */
public class FakeProductRepository implements ProductRepository{
    protected ArrayList<Product> productsSaved = new ArrayList<Product>();

    @Override
    public void saveProduct(Product product) {
        Product productToBeSaved = this.createProduct(product);
        this.productsSaved.add(productToBeSaved);
    }

    private Product createProduct(Product product) {
        Product productToBeSaved = new Product();
        productToBeSaved.setName(product.getName());
        productToBeSaved.setDescription(product.getDescription());
        productToBeSaved.setPrice(product.getPrice());
        productToBeSaved.setUnitsInStock(product.getUnitsInStock());
        productToBeSaved.setId(product.getId());
        return productToBeSaved;
    }

    @Override
    public Product getProductByName(String productName) {
        for (int i = 0; i < this.productsSaved.size(); i++) {
            if (this.productsSaved.get(i).getName().equalsIgnoreCase(productName)) {
                return this.productsSaved.get(i);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Product> getAllProductsSaved() {
        return productsSaved;
    }

    @Override
    public Product getProductById(String id) {
        for (int i = 0; i < this.productsSaved.size(); i++) {
            if (this.productsSaved.get(i).getId().equals(id)) {
                return this.productsSaved.get(i);
            }
        }
        return null;
    }

    @Override
    public void updateProduct(String productId, ProductInfo newProductInfo) {
        Product product = getProductById(productId);
        if (product != null) {
            product.setName(newProductInfo.name);
            product.setDescription(newProductInfo.description);
            product.setPrice(newProductInfo.price);
            product.setUnitsInStock(newProductInfo.unitsInStock);
        }
    }

    @Override
    public void deleteProductWithId(String productId) {
        Product productToDelete = getProductById(productId);
        if (productToDelete != null) {
            productsSaved.remove(productToDelete);
        }
    }
}
