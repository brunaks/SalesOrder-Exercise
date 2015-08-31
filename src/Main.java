import spark.Spark;

/**
 * Created by i848075 on 25/08/2015.
 */
public class Main {

    public static void main(String[] args) {
        ProductReceiver receiver = new FakeProductReceiver();
        ProductRepository repository = new FakeProductRepository();
        Spark.externalStaticFileLocation("resources/public");
        Spark.post("/registerProduct", new RegisterOneProduct(repository, receiver));
        Spark.get("/products", new ProductsRoute(repository));
        Spark.post("/updateProduct", new UpdateProductRoute(repository, receiver));
    }
}
