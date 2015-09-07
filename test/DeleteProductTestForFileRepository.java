import UseCases.Product.DeleteProductUseCase;
import org.junit.Before;

import java.io.File;

/**
 * Created by Bruna Koch Schmitt on 07/09/2015.
 */
public class DeleteProductTestForFileRepository extends DeleteProductTest{

    @Override
    public void setUp() throws Exception{
        repository = new FileProductRepository();
        receiver = new FakeProductReceiver();
        deleteProduct = new DeleteProductUseCase(repository, receiver);
        File file = new File("products.csv");
        file.delete();
    }
}
