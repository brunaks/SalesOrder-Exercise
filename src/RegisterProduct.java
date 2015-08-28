import java.util.UUID;

/**
 * Created by I848075 on 19/08/2015.
 */
public class RegisterProduct {
    private ProductReceiver productReceiver;
    private ProductRepository repository;
    private String name;
    private String description;
    private double price;
    private int unitsInStock;
    private Product product;

    public RegisterProduct(ProductReceiver productReceiver, ProductInfo productInfo, ProductRepository repository) {
        this.productReceiver = productReceiver;
        this.name = productInfo.name;
        this.description = productInfo.description;
        this.price = productInfo.price;
        this.unitsInStock = productInfo.unitsInStock;
        this.repository = repository;
    }

    public void execute() {
        if (productInformationIsValid()) {
            createProductAndSetInfo();
            saveProduct();
        } else {
            productReceiver.productInformationIsInvalid();
            productReceiver.registrationFailed();
        }
    }

    private void saveProduct() {
        Product aProduct = this.repository.getProductByName(this.name);
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
        product.setName(this.name);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setUnitsInStock(this.unitsInStock);
        product.setId(UUID.randomUUID().toString());
    }

    private boolean productInformationIsValid() {
        if (this.nameIsValid() && this.descriptionIsValid() && this.priceIsValid() && this.unitsInStockIsValid())
            return true;
        else
            return false;
    }

    private boolean unitsInStockIsValid() {
        return this.unitsInStock > 0;
    }

    private boolean priceIsValid() {
        return this.price > 0;
    }

    private boolean descriptionIsValid() {
        return !this.description.isEmpty();
    }

    private boolean nameIsValid() {
        return !this.name.isEmpty();
    }
}
