package UseCases.Product;

import Entities.Product.ProductInfo;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Receiver.FakeProductReceiver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by I848075 on 14/08/2015.
 */
public class RegisterProductTest {

    RegisterProductUseCase registerProduct;
    FakeProductReceiver productReceiver;
    ReadProductUseCase readProduct;
    private InMemoryProductRepository repository;

    @Before
    public void setUp() throws Exception {
        this.productReceiver = new FakeProductReceiver();
        this.repository = new InMemoryProductRepository();
    }

    @Test
    public void canRegisterProductWithSuccess() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 10);
        executeProductRegistration(pi);
        Assert.assertTrue(productReceiver.registrationWasSuccessful);
    }

    @Test
    public void productCouldNotBeRegistered_nameIsBlank() {
        ProductInfo pi = givenProductInfo("", "productDescription", 10.0, 10);
        executeProductRegistration(pi);
        Assert.assertFalse(productReceiver.registrationWasSuccessful);
    }

    @Test
    public void productCouldNotBeRegistered_descriptionIsBlank() {
        ProductInfo pi = givenProductInfo("productName", "", 10.0, 10);
        executeProductRegistration(pi);
        Assert.assertFalse(productReceiver.registrationWasSuccessful);
    }

    @Test
    public void productCouldNotBeRegistered_priceIsLowerThanZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", -1.0, 10);
        executeProductRegistration(pi);
        Assert.assertFalse(productReceiver.registrationWasSuccessful);
    }

    @Test
    public void productCouldNotBeRegistered_unitsInStockIsLowerThanZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, -1);
        executeProductRegistration(pi);
        Assert.assertFalse(productReceiver.registrationWasSuccessful);
    }

    @Test
    public void productCouldNotBeRegistered_unitsInStockIsEqualToZero() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 0);
        executeProductRegistration(pi);
        Assert.assertTrue(productReceiver.registrationWasSuccessful);
    }

    @Test
    public void productValidIsInTheRepository() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 10);
        executeProductRegistration(pi);
        readProduct = new ReadProductUseCase(this.repository, this.productReceiver);
        ProductInfo info = readProduct.getProductInfoByProductName("productName");
        Assert.assertNotNull(info);
    }

    @Test
    public void productInvalidIsNotInTheRepository() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, -1);
        executeProductRegistration(pi);
        readProduct = new ReadProductUseCase(this.repository, this.productReceiver);
        ProductInfo piRetrieved = readProduct.getProductInfoByProductName("productName");
        Assert.assertNull(piRetrieved);
    }

    @Test
    public void productValidIsInTheRepository_CanRetrieveInfo() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 10);
        executeProductRegistration(pi);
        readProduct = new ReadProductUseCase(this.repository, this.productReceiver);
        ProductInfo piRetrieved = readProduct.getProductInfoByProductName("productName");
        assertProductsInfoAreEqual(pi, piRetrieved);
    }

    @Test
    public void cannotSaveProductWithSameInfoAsAnotherOneAlreadySaved() {
        ProductInfo pi = givenProductInfo("productName", "productDescription", 10.0, 10);
        executeProductRegistration(pi);
        executeProductRegistration(pi);
        Assert.assertFalse(productReceiver.registrationWasSuccessful);
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
        registerProduct = new RegisterProductUseCase(productReceiver, pi, repository);
        registerProduct.execute();
    }

    private void assertProductsInfoAreEqual(ProductInfo pi, ProductInfo piRetrieved) {
        Assert.assertEquals(pi.name, piRetrieved.name);
        Assert.assertEquals(pi.description, piRetrieved.description);
        Assert.assertEquals(pi.price, piRetrieved.price, 0.001);
        Assert.assertEquals(pi.unitsInStock, piRetrieved.unitsInStock);
    }
}
