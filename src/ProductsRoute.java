import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by I848075 on 28/08/2015.
 */
public class ProductsRoute implements Route {
    private ProductRepository repository;

    public ProductsRoute(ProductRepository repository) {this.repository = repository;}

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ListProducts pl = new ListProducts(repository);
        Gson converter = new Gson();
        return converter.toJson(pl.returnsAllProducts());
    }
}
