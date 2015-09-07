package Routes.ProductRoutes;

import Entities.Product.ProductInfo;
import Interfaces.Receivers.ProductReceiver;
import Interfaces.Persistence.ProductRepository;
import Routes.RequestObjects.ProductInfoRequest;
import UseCases.Product.RegisterProductUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by i848075 on 26/08/2015.
 */
public class RegisterProductRoute implements Route{

    private ProductReceiver receiver;
    private ProductRepository repository;
    String name;
    String description;
    String price;
    String units;
    private ProductInfo productInfo;
    Gson converter = new Gson();

    public RegisterProductRoute(ProductRepository repository, ProductReceiver receiver) {
        this.repository = repository;
        this.receiver = receiver;
    }

    public Object handle(Request rq, Response rp) throws Exception {
        getRequestInfo(rq);
        createProductInfo();
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(receiver, productInfo, repository);
        registerProduct.execute();
        return converter.toJson(receiver);
    }

    private void getRequestInfo(Request rq) {
        ProductInfoRequest input = converter.fromJson(rq.body(), ProductInfoRequest.class);
        name = input.name;
        description = input.description;
        price = input.price;
        units = input.units;
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
}
