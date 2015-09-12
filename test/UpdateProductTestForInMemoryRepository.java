import UseCases.Product.UpdateProductUseCase;
import org.junit.Before;

/**
 * Created by Bruna Koch Schmitt on 07/09/2015.
 */
public class UpdateProductTestForInMemoryRepository extends UpdateProductTest {

    @Before
    public void setUp() throws Exception {
        repository = new InMemoryProductRepository();
        receiver = new FakeProductReceiver();
        updateProduct = new UpdateProductUseCase(repository, receiver);
    }
}
