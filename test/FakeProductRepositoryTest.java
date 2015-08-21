import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by I848075 on 21/08/2015.
 */
public class FakeProductRepositoryTest {

    FakeProductRepository repository;
    FakeProductReceiver receiver;

    @Before
    public void setUp() throws Exception {
        receiver = new FakeProductReceiver();
        repository = new FakeProductRepository(receiver);
    }

    @Test
    public void canSaveAndReadOneProduct() {
        Product product = givenProduct("productName", "productDescription", 10.0, 10);
        repository.saveProduct(product);
        Product productRetrieved = repository.getProductByName("productName");
        assertProductsAreEqual(product, productRetrieved);
    }

    private Product givenProduct(String productName, String productDescription, double price, int unitsInStock) {
        Product product = new Product();
        product.setName(productName);
        product.setDescription(productDescription);
        product.setPrice(price);
        product.setUnitsInStock(unitsInStock);
        return product;
    }

    private void assertProductsAreEqual(Product product, Product productRetrieved) {
        Assert.assertEquals(product.getName(), productRetrieved.getName());
        Assert.assertEquals(product.getDescription(), productRetrieved.getDescription());
        Assert.assertEquals(product.getPrice(), productRetrieved.getPrice(), 0.001);
        Assert.assertEquals(product.getUnitsInStock(), productRetrieved.getUnitsInStock());
    }
}
