import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public class UpdateProductRoute implements Route {
    private ProductRepository repository;
    private ProductReceiver receiver;

    public UpdateProductRoute(ProductRepository repository, ProductReceiver receiver) {
        this.repository = repository;
        this.receiver = receiver;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        UpdateProduct updateProduct = new UpdateProduct(repository, receiver);

        return null;
    }
}
