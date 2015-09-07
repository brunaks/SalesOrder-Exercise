package Routes.ProductRoutes;

import Interfaces.Receivers.ProductReceiver;
import Interfaces.Persistence.ProductRepository;
import Routes.RequestObjects.ProductInfoRequest;
import UseCases.Product.DeleteProductUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 31/08/2015.
 */
public class DeleteProductRoute implements Route {

    private ProductRepository repository;
    private ProductReceiver receiver;
    private Gson converter = new Gson();
    private String id;

    public DeleteProductRoute(ProductRepository repository, ProductReceiver receiver) {
        this.repository = repository;
        this.receiver = receiver;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        DeleteProductUseCase deleteProduct = new DeleteProductUseCase(this.repository, this.receiver);
        getRequestInfo(request);
        deleteProduct.executeWithId(this.id);
        return converter.toJson(receiver);
    }

    private void getRequestInfo(Request request) {
        ProductInfoRequest input = converter.fromJson(request.body(), ProductInfoRequest.class);
        this.id = input.id;
    }
}
