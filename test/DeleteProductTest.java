import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import UseCases.Product.DeleteProductUseCase;
import UseCases.Product.ListProductsUseCase;
import UseCases.Product.ReadProductUseCase;
import UseCases.Product.RegisterProductUseCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public class DeleteProductTest {

    public DeleteProductUseCase deleteProduct;
    public ProductRepository repository;
    public FakeProductReceiver receiver;

    @Before
    public void setUp() throws Exception {
        repository = new InMemoryProductRepository();
        receiver = new FakeProductReceiver();
        deleteProduct = new DeleteProductUseCase(repository, receiver);
    }

    @Test
    public void deleteFailed_ProductNotFound() {
        deleteProduct.executeWithId(UUID.randomUUID().toString());
        Assert.assertTrue(receiver.deleteFailed);
        assertSizeOfList(0);
    }

    @Test
    public void deleteSuccessful() {
        ProductInfo info = givenProduct("name", "description", 10, 10);
        registerProduct(info);
        assertSizeOfList(1);
        String id = getProductIdByName("name");
        deleteProduct.executeWithId(id);
        Assert.assertFalse(receiver.updateFailed);
        assertSizeOfList(0);
    }

    private void assertSizeOfList(int size) {
        ListProductsUseCase list = new ListProductsUseCase(repository);
        Assert.assertEquals(list.returnsAllProducts().size(), size);
    }


    private String getProductIdByName(String name) {
        ReadProductUseCase read = new ReadProductUseCase(repository, receiver);
        return read.getProductInfoByProductName(name).id;
    }

    private void registerProduct(ProductInfo info) {
        RegisterProductUseCase register = new RegisterProductUseCase(receiver, info, repository);
        register.execute();
    }

    private ProductInfo givenProduct(String name, String description, int price, int unitsInStock) {
        ProductInfo info = new ProductInfo();
        info.name = name;
        info.description = description;
        info.price = price;
        info.unitsInStock = unitsInStock;
        return info;
    }
}
