import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by I848075 on 14/08/2015.
 */
public class RegisterProductTest {

    RegisterProduct registerProduct;
    FakeProductReceiver productReceiver;
    ReadProduct readProduct;
    private FakeProductRepository repository;

    @Before
    public void setUp() throws Exception {
        this.productReceiver = new FakeProductReceiver();
        this.repository = new FakeProductRepository(productReceiver);
    }

    @Test
    public void canRegisterProductWithSuccess() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 10);
        createRegisterProductUseCase(pi);
        Assert.assertTrue(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_nameIsBlank() {
        ProductInfo pi = givenProductInfo("", "productDescription", 10.0, 10);
        createRegisterProductUseCase(pi);
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_descriptionIsBlank() {
        ProductInfo pi = givenProductInfo("productName", "", 10.0, 10);
        createRegisterProductUseCase(pi);
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_priceIsLowerThanZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", -1.0, 10);
        createRegisterProductUseCase(pi);
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_unitsInStockIsLowerThanZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, -1);
        createRegisterProductUseCase(pi);
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_unitsInStockIsEqualToZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 0);
        createRegisterProductUseCase(pi);
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productValidIsInTheRepository() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 10);
        createRegisterProductUseCase(pi);
        readProduct = new ReadProduct(this.repository, this.productReceiver);
        readProduct.getProductInfoByProductName("productName");
        Assert.assertTrue(productReceiver.productIsInRepository());
    }

    @Test
    public void productInvalidIsNotInTheRepository() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 0);
        createRegisterProductUseCase(pi);
        readProduct = new ReadProduct(this.repository, this.productReceiver);
        readProduct.getProductInfoByProductName("productName");
        Assert.assertFalse(productReceiver.productIsInRepository());
    }

    private ProductInfo givenProductInfo(String productName, String productDescription, double price, int unitsInStock) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.name = productName;
        productInfo.description = productDescription;
        productInfo.price = price;
        productInfo.unitsInStock = unitsInStock;
        return productInfo;
    }

    private void createRegisterProductUseCase(ProductInfo pi) {
        registerProduct = new RegisterProduct(productReceiver, pi, repository);
        registerProduct.execute();
    }
}
