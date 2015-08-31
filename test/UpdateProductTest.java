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
    public void updateSuccessful_onlyNameChanged() {
        ProductInfo oldInfo = givenProductInfo("name", "description", 2, 10);
        ProductInfo newInfo = givenProductInfo("name2", "description", 2, 10);

        RegisterProduct register = new RegisterProduct(receiver, oldInfo, repository);
        register.execute();

        updateProduct.withId(repository.getProductByName("name").getId());
        updateProduct.setNewProductInfo(newInfo);
        Assert.assertFalse(receiver.updateFailed);

        ReadProduct read = new ReadProduct(repository, receiver);
        ProductInfo retrievedInfo = read.getProductInfoById(repository.getProductByName("name2").getId());

        Assert.assertEquals(retrievedInfo.name, newInfo.name);
        Assert.assertEquals(retrievedInfo.description, oldInfo.description);
        Assert.assertEquals(retrievedInfo.price, oldInfo.price, 0.001);
        Assert.assertEquals(retrievedInfo.unitsInStock, oldInfo.unitsInStock);
    }

    @Test
    public void updateSuccessful_allValuesAreChanged() {
        ProductInfo oldInfo = givenProductInfo("name", "description", 2, 10);
        ProductInfo newInfo = givenProductInfo("name2", "description2", 3, 20);

        RegisterProduct register = new RegisterProduct(receiver, oldInfo, repository);
        register.execute();

        updateProduct.withId(repository.getProductByName("name").getId());
        updateProduct.setNewProductInfo(newInfo);
        Assert.assertFalse(receiver.updateFailed);

        ReadProduct read = new ReadProduct(repository, receiver);
        ProductInfo retrievedInfo = read.getProductInfoById(repository.getProductByName("name2").getId());

        Assert.assertEquals(retrievedInfo.name, newInfo.name);
        Assert.assertEquals(retrievedInfo.description, newInfo.description);
        Assert.assertEquals(retrievedInfo.price, newInfo.price, 0.001);
        Assert.assertEquals(retrievedInfo.unitsInStock, newInfo.unitsInStock);
    }

    @Test
    public void updateFailed_newInfoIsNotValid() {
        ProductInfo oldInfo = givenProductInfo("name", "description", 2, 10);
        ProductInfo newInfo = givenProductInfo("", "", 0, 0);

        RegisterProduct register = new RegisterProduct(receiver, oldInfo, repository);
        register.execute();

        updateProduct.withId(repository.getProductByName("name").getId());
        updateProduct.setNewProductInfo(newInfo);
        Assert.assertTrue(receiver.updateFailed);

        ReadProduct read = new ReadProduct(repository, receiver);
        ProductInfo retrievedInfo = read.getProductInfoById(repository.getProductByName("name").getId());

        Assert.assertEquals(retrievedInfo.name, oldInfo.name);
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
