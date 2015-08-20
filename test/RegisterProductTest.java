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
        productReceiver = new FakeProductReceiver();
    }

    @Test
    public void canRegisterProductWithSuccess() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 10);
        registerProduct = new RegisterProduct(productReceiver, pi);
        registerProduct.execute();
        Assert.assertTrue(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_nameIsBlank() {
        ProductInfo pi = givenProductInfo("", "productDescription", 10.0, 10);
        registerProduct = new RegisterProduct(productReceiver, pi);
        registerProduct.execute();
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_descriptionIsBlank() {
        ProductInfo pi = givenProductInfo("productName", "", 10.0, 10);
        registerProduct = new RegisterProduct(productReceiver, pi);
        registerProduct.execute();
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_priceIsLowerThanZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", -1.0, 10);
        registerProduct = new RegisterProduct(productReceiver, pi);
        registerProduct.execute();
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_unitsInStockIsLowerThanZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, -1);
        registerProduct = new RegisterProduct(productReceiver, pi);
        registerProduct.execute();
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_unitsInStockIsEqualToZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 0);
        registerProduct = new RegisterProduct(productReceiver, pi);
        registerProduct.execute();
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productValidIsInTheRepository() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 0);
        registerProduct = new RegisterProduct(productReceiver, pi);
        registerProduct.execute();
        readProduct = new ReadProduct(this.repository, this.productReceiver);
        readProduct.getProductByName("productName");
        Assert.assertTrue(productReceiver.productFound());
    }

    private ProductInfo givenProductInfo(String productName, String productDescription, double price, int unitsInStock) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.name = productName;
        productInfo.description = productDescription;
        productInfo.price = price;
        productInfo.unitsInStock = unitsInStock;
        return productInfo;
    }
}
