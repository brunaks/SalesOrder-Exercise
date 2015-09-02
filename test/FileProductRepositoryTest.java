import org.junit.Assert;
import org.junit.Test;

/**
 * Created by i848075 on 01/09/2015.
 */
public class FileProductRepositoryTest extends FakeProductRepositoryTest {

    @Override
    protected ProductRepository createRepository() {
        return new FileProductRepository();
    }

    @Test
    public void dataMustBePersistedIndependentlyFromTheRepositoryInstance() {
        ProductRepository repository  = new FileProductRepository();
        Product product = givenProduct("name", "description", 10, 10);
        repository.saveProduct(product);

        ProductRepository repository2 = new FileProductRepository();
        Product retrievedProduct = repository2.getProductByName("name");
        Assert.assertNotNull(retrievedProduct);
    }
}
