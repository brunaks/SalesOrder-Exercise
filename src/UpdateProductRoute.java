import com.google.gson.Gson;
import com.google.gson.JsonElement;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public class UpdateProductRoute implements Route {
    private ProductRepository repository;
    private ProductReceiver receiver;
    Gson converter = new Gson();
    private String productId;
    private String name;
    private String description;
    private String price;
    private String units;
    private ProductInfo productInfo;


    public UpdateProductRoute(ProductRepository repository, ProductReceiver receiver) {
        this.repository = repository;
        this.receiver = receiver;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        UpdateProduct updateProduct = new UpdateProduct(repository, receiver);
        getRequestInfo(request);
        updateProduct.withId(this.productId);
        createProductInfo();
        updateProduct.setNewProductInfo(this.productInfo);

        ReadProduct read = new ReadProduct(repository, receiver);
        ProductInfo info = read.getProductInfoById(this.productId);
        String jsonInfo = converter.toJson(info);
        String jsonReceiver = converter.toJson(receiver);

        jsonInfo = jsonInfo.substring(0, jsonInfo.length()-1);
        jsonReceiver = jsonReceiver.substring(1);

        return jsonInfo.concat(",").concat(jsonReceiver);
    }

    private void createProductInfo() {
        ProductInfo info = new ProductInfo();
        info.name = this.name;
        info.description = this.description;
        try {
            info.price = Double.parseDouble(this.price);
        } catch (NumberFormatException e) {
            info.price = 0.0;
        }
        try {
            info.unitsInStock = Integer.parseInt(this.units);
        } catch (NumberFormatException e) {
            info.unitsInStock = 0;
        }
        this.productInfo = info;
    }

    private void getRequestInfo(Request request) {
        ProductInfoRequest input = converter.fromJson(request.body(), ProductInfoRequest.class);
        productId = input.id;
        name = input.name;
        description = input.description;
        price = input.price;
        units = input.units;
    }
}
