package UseCases.Product;

import Entities.Product.Product;
import Entities.Product.ProductInfo;
import Interfaces.Receivers.ProductReceiver;
import Interfaces.Persistence.ProductRepository;

import java.util.UUID;

/**
 * Created by I848075 on 19/08/2015.
 */
public class RegisterProductUseCase {
    private ProductReceiver productReceiver;
    private ProductRepository repository;
    private ProductInfo productInfo = new ProductInfo();

    public RegisterProductUseCase(ProductReceiver productReceiver, ProductInfo productInfo, ProductRepository repository) {
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
            saveProduct();
        } else {
            productReceiver.productInformationIsInvalid();
            productReceiver.registrationFailed();
        }
    }

    private void saveProduct() {
        ProductInfo info = this.repository.getProductInfoByName(this.productInfo.name);
        if (info == null) {
            this.repository.saveProduct(this.productInfo);
            productReceiver.registrationWasSuccessful();
        }
        else {
            productReceiver.registrationFailed();
        }
    }
}
