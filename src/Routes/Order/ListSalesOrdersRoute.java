package Routes.Order;

import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.SalesOrderReceiver;
import UseCases.Order.ListSalesOrdersUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 26/11/2015.
 */
public class ListSalesOrdersRoute implements Route {
    private SalesOrderReceiver receiver;
    private SalesOrderRepository repository;

    public ListSalesOrdersRoute(SalesOrderRepository salesOrderRepository, SalesOrderReceiver salesOrderReceiver) {
        this.repository = salesOrderRepository;
        this.receiver = salesOrderReceiver;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ListSalesOrdersUseCase list = new ListSalesOrdersUseCase(repository);
        Gson converter = new Gson();
        return converter.toJson(list.getAllSummaries());
    }
}
