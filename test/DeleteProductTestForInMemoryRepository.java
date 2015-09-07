import UseCases.Product.DeleteProductUseCase;
import org.junit.Before;

/**
 * Created by Bruna Koch Schmitt on 07/09/2015.
 */
public class DeleteProductTestForInMemoryRepository extends DeleteProductTest {

    public void setUp() throws Exception{
        repository = new FakeProductRepository();
        receiver = new FakeProductReceiver();
        deleteProduct = new DeleteProductUseCase(repository, receiver);
    }
}
