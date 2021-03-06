package UseCases.Product;

import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import TestDoubles.Receiver.FakeProductReceiver;
import UseCases.Product.ReadProductUseCase;
import UseCases.Product.RegisterProductUseCase;
import UseCases.Product.UpdateProductUseCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public abstract class UpdateProductTest {

    UpdateProductUseCase updateProduct;
    ProductRepository repository;
    FakeProductReceiver receiver;

    @Before
    public abstract void setUp() throws Exception;

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

        RegisterProductUseCase register = new RegisterProductUseCase(receiver, oldInfo, repository);
        register.execute();

        updateProduct.withId(repository.getProductInfoByName("name").id);
        updateProduct.setNewProductInfo(newInfo);
        Assert.assertFalse(receiver.updateFailed);

        ReadProductUseCase read = new ReadProductUseCase(repository, receiver);
        ProductInfo retrievedInfo = read.getProductInfoById(repository.getProductInfoByName("name2").id);

        Assert.assertEquals(retrievedInfo.name, newInfo.name);
        Assert.assertEquals(retrievedInfo.description, oldInfo.description);
        Assert.assertEquals(retrievedInfo.price, oldInfo.price, 0.001);
        Assert.assertEquals(retrievedInfo.unitsInStock, oldInfo.unitsInStock);
    }

    @Test
    public void updateSuccessful_allValuesAreChanged() {
        ProductInfo oldInfo = givenProductInfo("name", "description", 2, 10);
        ProductInfo newInfo = givenProductInfo("name2", "description2", 3, 20);

        RegisterProductUseCase register = new RegisterProductUseCase(receiver, oldInfo, repository);
        register.execute();

        updateProduct.withId(repository.getProductInfoByName("name").id);
        updateProduct.setNewProductInfo(newInfo);
        Assert.assertFalse(receiver.updateFailed);

        ReadProductUseCase read = new ReadProductUseCase(repository, receiver);
        ProductInfo retrievedInfo = read.getProductInfoById(repository.getProductInfoByName("name2").id);

        Assert.assertEquals(retrievedInfo.name, newInfo.name);
        Assert.assertEquals(retrievedInfo.description, newInfo.description);
        Assert.assertEquals(retrievedInfo.price, newInfo.price, 0.001);
        Assert.assertEquals(retrievedInfo.unitsInStock, newInfo.unitsInStock);
    }

    @Test
    public void updateFailed_newInfoIsNotValid() {
        ProductInfo oldInfo = givenProductInfo("name", "description", 2, 10);
        ProductInfo newInfo = givenProductInfo("", "", 0, 0);

        RegisterProductUseCase register = new RegisterProductUseCase(receiver, oldInfo, repository);
        register.execute();

        updateProduct.withId(repository.getProductInfoByName("name").id);
        updateProduct.setNewProductInfo(newInfo);
        Assert.assertTrue(receiver.updateFailed);

        ReadProductUseCase read = new ReadProductUseCase(repository, receiver);
        ProductInfo retrievedInfo = read.getProductInfoById(repository.getProductInfoByName("name").id);

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
