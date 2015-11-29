package Routes.Product;

import Interfaces.Persistence.ProductRepository;
import UseCases.Product.ListProductsUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by I848075 on 28/08/2015.
 */
public class ListProductsRoute implements Route {
    private ProductRepository repository;

    public ListProductsRoute(ProductRepository repository) {this.repository = repository;}

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ListProductsUseCase pl = new ListProductsUseCase(repository);
        Gson converter = new Gson();
        return converter.toJson(pl.returnsAllProducts());
    }
}
