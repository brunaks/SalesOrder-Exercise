import java.util.UUID;

/**
 * Created by I848075 on 19/08/2015.
 */
public class RegisterProduct {
    private ProductReceiver productReceiver;
    private ProductRepository repository;
    private Product product;
    private ProductInfo productInfo = new ProductInfo();

    public RegisterProduct(ProductReceiver productReceiver, ProductInfo productInfo, ProductRepository repository) {
        this.productReceiver = productReceiver;
        this.repository = repository;
        copyProductInfo(productInfo);
    }

    private void copyProductInfo(ProductInfo productInfo) {
        this.productInfo.name = productInfo.name;
        this.productInfo.description = productInfo.description;
        this.productInfo.price = productInfo.price;
        this.productInfo.unitsInStock = productInfo.unitsInStock;
    }

    public void execute() {
        if (productInfo.isValid()) {
            createProductAndSetInfo();
            saveProduct();
        } else {
            productReceiver.productInformationIsInvalid();
            productReceiver.registrationFailed();
        }
    }

    private void saveProduct() {
        Product aProduct = this.repository.getProductByName(this.productInfo.name);
        if (aProduct == null) {
            this.repository.saveProduct(this.product);
            productReceiver.registrationWasSuccessful();
        }
        else {
            productReceiver.registrationFailed();
        }
    }

    private void createProductAndSetInfo() {
        this.product = new Product();
        product.setName(this.productInfo.name);
        product.setDescription(this.productInfo.description);
        product.setPrice(this.productInfo.price);
        product.setUnitsInStock(this.productInfo.unitsInStock);
        product.setId(UUID.randomUUID().toString());
    }
}
