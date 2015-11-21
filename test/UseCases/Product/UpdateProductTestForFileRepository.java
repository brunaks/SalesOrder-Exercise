package UseCases.Product;

import TestDoubles.Persistence.FileProductRepository;
import TestDoubles.Receiver.FakeProductReceiver;
import org.junit.Before;

import java.io.File;

/**
 * Created by Bruna Koch Schmitt on 07/09/2015.
 */
public class UpdateProductTestForFileRepository extends UpdateProductTest {

    @Before
    public void setUp() throws Exception {
        File file = new File("products.csv");
        file.delete();
        repository = new FileProductRepository();
        receiver = new FakeProductReceiver();
        updateProduct = new UpdateProductUseCase(repository, receiver);
    }
}
