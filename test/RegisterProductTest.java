import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by I848075 on 14/08/2015.
 */
public class RegisterProductTest {

    RegisterProduct registerProduct;
    FakeProductReceiver productReceiver;

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
    public void productCouldNotBeRegistered() {
        ProductInfo pi = givenProductInfo("", "productDescription", 10.0, 10);
        registerProduct = new RegisterProduct(productReceiver, pi);
        registerProduct.execute();
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
}
