package Routes.Product;

import Entities.Product.ProductInfo;
import Interfaces.Receivers.ProductReceiver;
import Interfaces.Persistence.ProductRepository;
import Routes.RequestObjects.ProductInfoRequest;
import UseCases.Product.ReadProductUseCase;
import UseCases.Product.UpdateProductUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 30/08/2015.
 */
public class UpdateProductRoute implements Route {
    Gson converter = new Gson();
    private ProductRepository repository;
    private ProductReceiver receiver;
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
        UpdateProductUseCase updateProduct = new UpdateProductUseCase(repository, receiver);
        getRequestInfo(request);
        updateProduct.withId(this.productId);
        createProductInfo();
        updateProduct.setNewProductInfo(this.productInfo);

        ReadProductUseCase read = new ReadProductUseCase(repository, receiver);
        ProductInfo info = read.getProductInfoById(this.productId);
        String jsonInfo = converter.toJson(info);
        String jsonReceiver = converter.toJson(receiver);

        jsonInfo = jsonInfo.substring(0, jsonInfo.length()-1);
        jsonReceiver = jsonReceiver.substring(1);

        return jsonInfo.concat(",").concat(jsonReceiver);
    }

    private void createProductInfo() {
        ProductInfo info = new ProductInfo();
        info.id = this.productId;
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
