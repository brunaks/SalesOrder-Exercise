import Entities.Product.Product;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by I848075 on 20/08/2015.
 */
public class InMemoryProductRepository implements ProductRepository {
    protected ArrayList<Product> productsSaved = new ArrayList<Product>();

    @Override
    public void saveProduct(ProductInfo productInfo) {
        productInfo.id = createProductInfoID();
        Product productToBeSaved = this.createProduct(productInfo);
        this.productsSaved.add(productToBeSaved);
    }

    private Product createProduct(ProductInfo productInfo) {
        Product productToBeSaved = new Product();
        productToBeSaved.setName(productInfo.name);
        productToBeSaved.setDescription(productInfo.description);
        productToBeSaved.setPrice(productInfo.price);
        productToBeSaved.setUnitsInStock(productInfo.unitsInStock);
        productToBeSaved.setId(productInfo.id);
        return productToBeSaved;
    }

    @Override
    public ProductInfo getProductInfoByName(String productName) {
        for (int i = 0; i < this.productsSaved.size(); i++) {
            if (this.productsSaved.get(i).getName().equalsIgnoreCase(productName)) {
                return createProductInfo(this.productsSaved.get(i));
            }
        }
        return null;
    }

    private ProductInfo createProductInfo(Product product) {
        ProductInfo info = new ProductInfo();
        info.id = product.getId();
        info.name = product.getName();
        info.description = product.getDescription();
        info.price = product.getPrice();
        info.unitsInStock = product.getUnitsInStock();
        return info;
    }

    @Override
    public ArrayList<ProductInfo> getAllProductsInfoSaved() {
        ArrayList<ProductInfo> productsInfo = new ArrayList<ProductInfo>();
        for (int i = 0; i < this.productsSaved.size(); i++) {
            productsInfo.add(createProductInfo(productsSaved.get(i)));
        }
        return productsInfo;
    }

    @Override
    public ProductInfo getProductInfoById(String id) {
        for (int i = 0; i < this.productsSaved.size(); i++) {
            if (this.productsSaved.get(i).getId().equals(id)) {
                return createProductInfo(this.productsSaved.get(i));
            }
        }
        return null;
    }

    private Product getProductById(String id) {
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

    @Override
    public String createProductInfoID() {
        return UUID.randomUUID().toString();
    }
}
