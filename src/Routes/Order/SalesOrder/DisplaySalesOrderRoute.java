package Routes.Order.SalesOrder;

import Entities.Order.SalesOrderInfo;
import Interfaces.Persistence.SalesOrderRepository;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 29/11/2015.
 */
public class DisplaySalesOrderRoute implements Route {

    private SalesOrderRepository salesOrderRepository;
    private Gson converter = new Gson();

    public DisplaySalesOrderRoute(SalesOrderRepository salesOrderRepository) {
        this.salesOrderRepository = salesOrderRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return converter.toJson(makeSalesOrderInfo(request));
    }

    private SalesOrderInfo makeSalesOrderInfo(Request request) {
        return salesOrderRepository.getById(request.queryString());
    }
}
