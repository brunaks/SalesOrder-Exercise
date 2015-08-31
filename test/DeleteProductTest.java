import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public class DeleteProductTest {

    public DeleteProduct deleteProduct;
    public FakeProductRepository repository;
    public FakeProductReceiver receiver;

    @Before
    public void setUp() throws Exception {
        repository = new FakeProductRepository();
        receiver = new FakeProductReceiver();
        deleteProduct = new DeleteProduct(repository, receiver);
    }

    @Test
    public void deleteFailed_ProductNotFound() {
        deleteProduct.executeWithId(UUID.randomUUID().toString());
        Assert.assertTrue(receiver.deleteFailed);
        assertSizeOfList(0);
    }

    private void assertSizeOfList(int size) {
        ListProducts list = new ListProducts(repository);
        Assert.assertEquals(list.returnsAllProducts().size(), size);
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

    private String getProductIdByName(String name) {
        ReadProduct read = new ReadProduct(repository, receiver);
        return read.getProductInfoByProductName(name).id;
    }

    private void registerProduct(ProductInfo info) {
        RegisterProduct register = new RegisterProduct(receiver, info, repository);
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
