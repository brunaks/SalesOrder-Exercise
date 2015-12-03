package Routes.Order.PurchaseOrder;

import Entities.Order.PurchaseOrderInfo;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Receivers.PurchaseOrderReceiver;
import Routes.RequestObjects.PurchaseOrderRequest;
import UseCases.Order.CreatePurchaseOrderUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 03/12/2015.
 */
public class CreatePurchaseOrderRoute implements Route {

    Gson converter = new Gson();
    private ProductRepository productRepository;
    private PurchaseOrderReceiver receiver;
    private PurchaseOrderRepository repository;
    private String id;
    private Date order_date;
    private PurchaseOrderInfo orderInfo;
    private CreatePurchaseOrderUseCase createOrder;

    public CreatePurchaseOrderRoute(PurchaseOrderRepository purchaseOrderRepository, PurchaseOrderReceiver purchaseOrderReceiver, ProductRepository repository) {
        this.repository = purchaseOrderRepository;
        this.receiver = purchaseOrderReceiver;
        this.productRepository = repository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        getRequestInfo(request);
        createOrderInfo();

        createOrder = new CreatePurchaseOrderUseCase(orderInfo.id, repository, productRepository, receiver, this.order_date);
        createOrder.execute();
        return converter.toJson(receiver);
    }

    private void createOrderInfo() {
        this.orderInfo = new PurchaseOrderInfo();
        orderInfo.id = this.id;
        orderInfo.date = this.order_date;
    }

    private void getRequestInfo(Request rq) {
        PurchaseOrderRequest input = converter.fromJson(rq.body(), PurchaseOrderRequest.class);
        this.id = UUID.randomUUID().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.order_date = formatter.parse(input.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
