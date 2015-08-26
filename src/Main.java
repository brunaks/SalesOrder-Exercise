import spark.Spark;

/**
 * Created by i848075 on 25/08/2015.
 */
public class Main {

    public static void main(String[] args) {
        ProductRepository repository = new FakeProductRepository(new FakeProductReceiver());
        Spark.staticFileLocation("public");
        Spark.get("/registerProduct", new RegisterOneProduct(repository));

    }

}
