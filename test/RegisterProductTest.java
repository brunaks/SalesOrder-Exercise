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
        executeProductRegistration(pi);
        Assert.assertTrue(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_nameIsBlank() {
        ProductInfo pi = givenProductInfo("", "productDescription", 10.0, 10);
        executeProductRegistration(pi);
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_descriptionIsBlank() {
        ProductInfo pi = givenProductInfo("productName", "", 10.0, 10);
        executeProductRegistration(pi);
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_priceIsLowerThanZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", -1.0, 10);
        executeProductRegistration(pi);
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_unitsInStockIsLowerThanZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, -1);
        executeProductRegistration(pi);
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productCouldNotBeRegistered_unitsInStockIsEqualToZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 0);
        executeProductRegistration(pi);
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    @Test
    public void productValidIsInTheRepository() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 10);
        executeProductRegistration(pi);
        readProduct = new ReadProduct(this.repository, this.productReceiver);
        readProduct.getProductInfoByProductName("productName");
        Assert.assertTrue(productReceiver.productIsInRepository());
    }

    @Test
    public void productInvalidIsNotInTheRepository() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 0);
        executeProductRegistration(pi);
        readProduct = new ReadProduct(this.repository, this.productReceiver);
        ProductInfo piRetrieved = readProduct.getProductInfoByProductName("productName");
        Assert.assertFalse(productReceiver.productIsInRepository());
        Assert.assertNull(piRetrieved);
    }

    @Test
    public void productValidIsInTheRepository_CanRetrieveInfo() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 10);
        executeProductRegistration(pi);
        readProduct = new ReadProduct(this.repository, this.productReceiver);
        ProductInfo piRetrieved = readProduct.getProductInfoByProductName("productName");
        Assert.assertTrue(productReceiver.productIsInRepository());
        assertProductsInfoAreEqual(pi, piRetrieved);
    }

    @Test
    public void cannotSaveProductWithSameInfoAsAnotherOneAlreadySaved() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 10);
        executeProductRegistration(pi);
        executeProductRegistration(pi);
        Assert.assertFalse(productReceiver.productWasRegisteredSuccessfully());
    }

    private ProductInfo givenProductInfo(String productName, String productDescription, double price, int unitsInStock) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.name = productName;
        productInfo.description = productDescription;
        productInfo.price = price;
        productInfo.unitsInStock = unitsInStock;
        return productInfo;
    }

    private void executeProductRegistration(ProductInfo pi) {
        registerProduct = new RegisterProduct(productReceiver, pi, repository);
        registerProduct.execute();
    }

    private void assertProductsInfoAreEqual(ProductInfo pi, ProductInfo piRetrieved) {
        Assert.assertEquals(pi.name, piRetrieved.name);
        Assert.assertEquals(pi.description, piRetrieved.description);
        Assert.assertEquals(pi.price, piRetrieved.price, 0.001);
        Assert.assertEquals(pi.unitsInStock, piRetrieved.unitsInStock);
    }
}
