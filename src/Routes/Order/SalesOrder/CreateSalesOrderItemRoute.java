package Routes.Order.SalesOrder;

import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Persistence.SumToReceiveRepository;
import Interfaces.Receivers.SalesOrderReceiver;
import Routes.RequestObjects.CreateSalesOrderItemRequest;
import UseCases.Order.Sales.AddSalesOrderItemUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 26/11/2015.
 */
public class CreateSalesOrderItemRoute implements Route {
    private SalesOrderReceiver receiver;
    private ProductRepository productRepositoty;
    private SalesOrderRepository salesOrderRepository;
    private SumToReceiveRepository sumToReceiveRepository;
    private Gson converter = new Gson();

    public CreateSalesOrderItemRoute(SalesOrderRepository salesOrderRepository, ProductRepository productRepository, SalesOrderReceiver receiver, SumToReceiveRepository sumToReceiveRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.productRepositoty = productRepository;
        this.receiver = receiver;
        this.sumToReceiveRepository = sumToReceiveRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        CreateSalesOrderItemRequest createRequest = converter.fromJson(request.body(), CreateSalesOrderItemRequest.class);
        AddSalesOrderItemUseCase addItem = new AddSalesOrderItemUseCase(createRequest.orderId,
                salesOrderRepository,
                productRepositoty,
                receiver,
                sumToReceiveRepository);
        addItem.withProductIdAndQuantity(createRequest.productId, createRequest.quantity);
        return converter.toJson(receiver);
    }
}
