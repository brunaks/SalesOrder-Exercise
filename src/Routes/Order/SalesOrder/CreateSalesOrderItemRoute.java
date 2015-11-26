package Routes.Order.SalesOrder;

import Entities.Order.SalesOrderInfo;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.SalesOrderReceiver;
import Routes.RequestObjects.CreateSalesOrderItemRequest;
import Routes.RequestObjects.SalesOrderSummary;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 26/11/2015.
 */
public class CreateSalesOrderItemRoute implements Route {
    private SalesOrderRepository salesOrderRepository;
    private Gson converter = new Gson();

    public CreateSalesOrderItemRoute(SalesOrderRepository salesOrderRepository) {
        this.salesOrderRepository = salesOrderRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        CreateSalesOrderItemRequest createRequest = converter.fromJson(request.body(), CreateSalesOrderItemRequest.class);
        salesOrderRepository.createItem(createRequest);
        return converter.toJson(null);
    }
}
