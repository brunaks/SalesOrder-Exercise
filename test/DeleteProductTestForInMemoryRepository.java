import UseCases.Product.DeleteProductUseCase;

/**
 * Created by Bruna Koch Schmitt on 07/09/2015.
 */
public class DeleteProductTestForInMemoryRepository extends DeleteProductTest {

    public void setUp() throws Exception{
        repository = new InMemoryProductRepository();
        receiver = new FakeProductReceiver();
        deleteProduct = new DeleteProductUseCase(repository, receiver);
    }
}
