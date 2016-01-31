package Routes.Order.SalesOrder;

import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Persistence.SumToReceiveRepository;
import Interfaces.Receivers.SalesOrderReceiver;
import Routes.RequestObjects.UpdateOrderStatusRequest;
import UseCases.Order.Sales.AddSalesOrderItemUseCase;
import UseCases.Order.Sales.UpdateSalesOrderStatusUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 01/12/2015.
 */
public class UpdateSalesOrderStatusRoute implements Route {

    private SalesOrderReceiver receiver;
    private SalesOrderRepository repository;
    private Gson converter = new Gson();
    private UpdateOrderStatusRequest updateRequest;
    private ProductRepository productRepository;
    private SumToReceiveRepository sumToReceiveRepository;

    public UpdateSalesOrderStatusRoute(SalesOrderRepository salesOrderRepository, SalesOrderReceiver salesOrderReceiver, ProductRepository productRepository, SumToReceiveRepository sumToReceiveRepository) {
        this.repository = salesOrderRepository;
        this.receiver = salesOrderReceiver;
        this.productRepository = productRepository;
        this.sumToReceiveRepository = sumToReceiveRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        getRequestInfo(request);
        if (updateRequest.newStatus.equalsIgnoreCase("InProcess")) {
            AddSalesOrderItemUseCase addItem = new AddSalesOrderItemUseCase(updateRequest.orderId,
                    this.repository,
                    this.productRepository,
                    this.receiver,
                    sumToReceiveRepository);
            addItem.setOrderToProcessing();
        } else {
            UpdateSalesOrderStatusUseCase updateStatus = new UpdateSalesOrderStatusUseCase(updateRequest.orderId, this.repository, this.receiver);
            updateStatus.changeTo(updateRequest.newStatus);
        }
        return converter.toJson(this.receiver);
    }

    private void getRequestInfo(Request request) {
        updateRequest = converter.fromJson(request.body(), UpdateOrderStatusRequest.class);
    }
}
