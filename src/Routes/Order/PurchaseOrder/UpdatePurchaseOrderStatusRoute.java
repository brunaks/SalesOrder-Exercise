package Routes.Order.PurchaseOrder;

import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Receivers.PurchaseOrderReceiver;
import Routes.RequestObjects.UpdateOrderStatusRequest;
import UseCases.Order.Purchase.AddPurchaseOrderItemUseCase;
import UseCases.Order.Purchase.UpdatePurchaseOrderStatusUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 03/12/2015.
 */
public class UpdatePurchaseOrderStatusRoute implements Route {
    private PurchaseOrderReceiver receiver;
    private PurchaseOrderRepository repository;
    private Gson converter = new Gson();
    private UpdateOrderStatusRequest updateRequest;
    private ProductRepository productRepository;

    public UpdatePurchaseOrderStatusRoute(PurchaseOrderRepository purchaseOrderRepository, PurchaseOrderReceiver purchaseOrderReceiver, ProductRepository productRepository) {
        this.repository = purchaseOrderRepository;
        this.receiver = purchaseOrderReceiver;
        this.productRepository = productRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        getRequestInfo(request);
        if (updateRequest.newStatus.equalsIgnoreCase("InProcess")) {
            AddPurchaseOrderItemUseCase addItem = new AddPurchaseOrderItemUseCase(updateRequest.orderId,
                    this.repository,
                    this.productRepository,
                    this.receiver);
            addItem.setOrderToProcessing();
        } else {
            UpdatePurchaseOrderStatusUseCase updateStatus = new UpdatePurchaseOrderStatusUseCase(updateRequest.orderId, this.repository, this.receiver);
            updateStatus.changeTo(updateRequest.newStatus);
        }
        return converter.toJson(this.receiver);
    }

    private void getRequestInfo(Request request) {
        updateRequest = converter.fromJson(request.body(), UpdateOrderStatusRequest.class);
    }

}
