package Routes.Order.PurchaseOrder;

import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Receivers.PurchaseOrderReceiver;
import Routes.RequestObjects.CreatePurchaseOrderItemRequest;
import UseCases.Order.Purchase.AddPurchaseOrderItemUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 03/12/2015.
 */
public class CreatePurchaseOrderItemRoute implements Route {

    private PurchaseOrderReceiver receiver;
    private ProductRepository productRepositoty;
    private PurchaseOrderRepository purchaseOrderRepository;
    private Gson converter = new Gson();

    public CreatePurchaseOrderItemRoute(PurchaseOrderRepository purchaseOrderRepository, ProductRepository productRepository, PurchaseOrderReceiver receiver) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productRepositoty = productRepository;
        this.receiver = receiver;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        CreatePurchaseOrderItemRequest createRequest = converter.fromJson(request.body(), CreatePurchaseOrderItemRequest.class);
        AddPurchaseOrderItemUseCase addItem = new AddPurchaseOrderItemUseCase(createRequest.orderId,
                purchaseOrderRepository,
                productRepositoty,
                receiver);
        addItem.withProductIdAndQuantity(createRequest.productId, createRequest.quantity);
        return converter.toJson(receiver);
    }
}
