/**
 * Created by I848075 on 19/08/2015.
 */
public class RegisterProduct {
    private final ProductReceiver productReceiver;
    public String name;
    public String description;
    public double price;
    public int unitsInStock;

    public RegisterProduct(ProductReceiver productReceiver, ProductInfo productInfo) {
        this.productReceiver = productReceiver;
        this.name = productInfo.name;
        this.description = productInfo.description;
        this.price = productInfo.price;
        this.unitsInStock = productInfo.unitsInStock;
    }

    public void execute() {
        if (productInformationIsValid()) {
            createProductAndSetInfo();
            productReceiver.RegistrationWasSuccessful();
        } else {
            productReceiver.productInformationIsInvalid();
            productReceiver.registrationFailed();
        }
    }

    private void createProductAndSetInfo() {
        Product product = new Product();
        product.setName(this.name);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setUnitsInStock(this.unitsInStock);
    }

    private boolean productInformationIsValid() {
        if (this.nameIsValid() && this.descriptionIsValid() && this.priceIsValid() && this.unitsInStockIsValid())
            return true;
        else
            return false;
    }

    private boolean unitsInStockIsValid() {
        return true;
    }

    private boolean priceIsValid() {
        return true;
    }

    private boolean descriptionIsValid() {
        return true;
    }

    private boolean nameIsValid() {
        return !this.name.isEmpty();
    }
}
