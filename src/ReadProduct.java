/**
 * Created by I848075 on 20/08/2015.
 */
public class ReadProduct {
    private ProductRepository repository;
    private ProductReceiver productReceiver;

    public ReadProduct(ProductRepository repository, ProductReceiver productReceiver) {
        this.repository = repository;
        this.productReceiver = productReceiver;
    }

    public ProductInfo getProductInfoByProductName(String productName) {
        Product product = this.repository.getProductByName(productName);
        if (product != null)
            return buildProductInfo(product);
        else
            return null;
    }

    private ProductInfo buildProductInfo(Product product) {
        ProductInfo info = new ProductInfo();
        info.id = product.getId();
        info.name = product.getName();
        info.description = product.getDescription();
        info.price = product.getPrice();
        info.unitsInStock = product.getUnitsInStock();
        return info;
    }

    public ProductInfo getProductInfoById(String id) {
        Product product = this.repository.getProductById(id);
        if (product != null)
            return buildProductInfo(product);
        else
            return null;
    }
}
