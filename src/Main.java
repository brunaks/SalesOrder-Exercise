import Interfaces.Persistence.ProductRepository;
import Interfaces.Receivers.ProductReceiver;
import Routes.ProductRoutes.DeleteProductRoute;
import Routes.ProductRoutes.ListProductsRoute;
import Routes.ProductRoutes.RegisterProductRoute;
import Routes.ProductRoutes.UpdateProductRoute;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Receiver.FakeProductReceiver;
import spark.Spark;

/**
 * Created by i848075 on 25/08/2015.
 */
public class Main {

    public static void main(String[] args) {
        ProductReceiver receiver = new FakeProductReceiver();
        ProductRepository repository = new InMemoryProductRepository();
        Spark.externalStaticFileLocation("resources/public");
        Spark.post("/registerProduct", new RegisterProductRoute(repository, receiver));
        Spark.get("/products", new ListProductsRoute(repository));
        Spark.post("/updateProduct", new UpdateProductRoute(repository, receiver));
        Spark.post("/deleteProduct", new DeleteProductRoute(repository, receiver));
    }
}
