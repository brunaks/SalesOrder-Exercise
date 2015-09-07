import Entities.Product.Product;
import Interfaces.Persistence.ProductRepository;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by i848075 on 01/09/2015.
 */
public class FileProductRepositoryTest extends FakeProductRepositoryTest {

    protected ProductRepository createRepository() {
        return new FileProductRepository();
    }

    @Test
    public void dataMustBePersistedIndependentlyFromTheRepositoryInstance() {
        File file = new File("products.csv");
        file.delete();

        ProductRepository repository  = new FileProductRepository();
        Product product = givenProduct("name", "description", 10, 10);
        repository.saveProduct(product);

        ProductRepository repository2 = new FileProductRepository();
        Product retrievedProduct = repository2.getProductByName("name");
        Assert.assertNotNull(retrievedProduct);
    }

    private Product givenProduct(String name, String description, int price, int unitsInStock) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setUnitsInStock(unitsInStock);
        return product;
    }

    @Test
    public void fileContentsAreOverwrittenEveryTimeWriteObjectIsCalled() {
        File file = new File("products.csv");
        file.delete();

        ProductRepository repository  = new FileProductRepository();
        Product product = givenProduct("name", "description", 10, 10);
        repository.saveProduct(product);

        Product product2 = givenProduct("name2", "description2", 20, 20);
        repository.saveProduct(product2);

        Assert.assertEquals(2, repository.getAllProductsSaved().size());

    }
}
