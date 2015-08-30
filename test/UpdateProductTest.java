import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public class UpdateProductTest {

    UpdateProduct updateProduct;
    ProductRepository repository;
    FakeProductReceiver receiver;

    @Before
    public void setUp() throws Exception {
        repository = new FakeProductRepository();
        receiver = new FakeProductReceiver();
        updateProduct = new UpdateProduct(repository, receiver);
    }

    @Test
    public void updateFailed_productIdNotValid() {
        updateProduct.withId(UUID.randomUUID().toString());
        updateProduct.setNewProductInfo(new ProductInfo());
        Assert.assertTrue(receiver.updateFailed);
    }

    @Test
    public void updateSuccessful_nameChanged() {
        ProductInfo oldInfo = givenProductInfo("name", "description", 2, 10);
        ProductInfo newInfo = givenProductInfo("name2", "description", 2, 10);

        RegisterProduct register = new RegisterProduct(receiver, oldInfo, repository);
        register.execute();

        updateProduct.withId(oldInfo.id);
        updateProduct.setNewProductInfo(newInfo);
        Assert.assertFalse(receiver.updateFailed);

        ReadProduct read = new ReadProduct(repository, receiver);
        ProductInfo retrievedInfo = read.getProductInfoById(oldInfo.id);

        Assert.assertEquals(retrievedInfo.name, newInfo.name);
        Assert.assertEquals(retrievedInfo.description, oldInfo.description);
        Assert.assertEquals(retrievedInfo.price, oldInfo.price, 0.001);
        Assert.assertEquals(retrievedInfo.unitsInStock, oldInfo.unitsInStock);
    }

    private ProductInfo givenProductInfo(String name, String description, int price, int unitsInStock) {
        ProductInfo pi = new ProductInfo();
        pi.name = name;
        pi.description = description;
        pi.price = price;
        pi.unitsInStock = unitsInStock;
        pi.id = UUID.randomUUID().toString();
        return pi;
    }


}
