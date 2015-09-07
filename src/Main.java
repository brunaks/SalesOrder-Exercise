import Interfaces.Receivers.ProductReceiver;
import Interfaces.Persistence.ProductRepository;
import Routes.ProductRoutes.DeleteProductRoute;
import Routes.ProductRoutes.ListProductsRoute;
import Routes.ProductRoutes.RegisterProductRoute;
import Routes.ProductRoutes.UpdateProductRoute;
import spark.Spark;

/**
 * Created by i848075 on 25/08/2015.
 */
public class Main {

    public static void main(String[] args) {
        ProductReceiver receiver = new FakeProductReceiver();
        ProductRepository repository = new FakeProductRepository();
        Spark.externalStaticFileLocation("resources/public");
        Spark.post("/registerProduct", new RegisterProductRoute(repository, receiver));
        Spark.get("/products", new ListProductsRoute(repository));
        Spark.post("/updateProduct", new UpdateProductRoute(repository, receiver));
        Spark.post("/deleteProduct", new DeleteProductRoute(repository, receiver));
    }
}
