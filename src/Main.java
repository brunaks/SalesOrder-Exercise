import spark.Spark;

import javax.sound.midi.Receiver;

/**
 * Created by i848075 on 25/08/2015.
 */
public class Main {

    public static void main(String[] args) {
        ProductReceiver receiver = new FakeProductReceiver();
        ProductRepository repository = new FakeProductRepository();
        Spark.staticFileLocation("public");
        Spark.post("/registerProduct", new RegisterOneProduct(repository, receiver));
    }

}
