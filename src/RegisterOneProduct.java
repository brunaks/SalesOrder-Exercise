import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by i848075 on 26/08/2015.
 */
public class RegisterOneProduct implements Route{

    private ProductReceiver receiver;
    private ProductRepository repository;
    String name;
    String description;
    String price;
    String units;
    private ProductInfo productInfo;
    Gson converter = new Gson();

    public RegisterOneProduct(ProductRepository repository, ProductReceiver receiver) {
        this.repository = repository;
        this.receiver = receiver;
    }

    public Object handle(Request rq, Response rp) throws Exception {
        getRequestInfo(rq);
        createProductInfo();
        RegisterProduct registerProduct = new RegisterProduct(receiver, productInfo, repository);
        registerProduct.execute();
        return converter.toJson(receiver);
    }

    private void getRequestInfo(Request rq) {
        name = rq.queryParams("name");
        description = rq.queryParams("description");
        price = rq.queryParams("price");
        units = rq.queryParams("units");
    }

    private void createProductInfo() {
        ProductInfo info = new ProductInfo();
        info.name = this.name;
        info.description = this.description;
        info.price = Double.parseDouble(this.price);
        info.unitsInStock = Integer.parseInt(this.units);
        this.productInfo = info;
    }
}
