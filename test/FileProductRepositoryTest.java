import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

/**
 * Created by i848075 on 01/09/2015.
 */
public class FileProductRepositoryTest extends ProductRepositoryTest {

    protected ProductRepository createRepository() {
        deleteTestFile();
        return new FileProductRepository();
    }

    public void deleteTestFile() {
        File file = new File("products.csv");
        file.delete();
    }

    @Test
    public void dataMustBePersistedIndependentlyFromTheRepositoryInstance() {

        ProductRepository repository  = new FileProductRepository();
        ProductInfo productInfo = givenProductInfo(UUID.randomUUID().toString(), "name", "description", 10, 10);
        repository.saveProduct(productInfo);

        ProductRepository repository2 = new FileProductRepository();
        ProductInfo retrievedProductInfo = repository2.getProductInfoByName("name");
        Assert.assertNotNull(retrievedProductInfo);
    }

    @Test
    public void fileContentsAreOverwrittenEveryTimeWriteObjectIsCalled() {
        File file = new File("products.csv");
        file.delete();

        ProductRepository repository  = new FileProductRepository();
        ProductInfo productInfo = givenProductInfo(UUID.randomUUID().toString(), "name", "description", 10, 10);
        repository.saveProduct(productInfo);

        ProductInfo productInfo2 = givenProductInfo(UUID.randomUUID().toString(), "name2", "description2", 20, 20);
        repository.saveProduct(productInfo2);

        Assert.assertEquals(2, repository.getAllProductsInfoSaved().size());
    }
}
