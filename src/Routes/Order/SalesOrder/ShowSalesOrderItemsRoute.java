package Routes.Order.SalesOrder;

import Entities.Order.OrderItem;
import Interfaces.Persistence.SalesOrderRepository;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;

/**
 * Created by Bruna Koch Schmitt on 26/11/2015.
 */
public class ShowSalesOrderItemsRoute implements Route {
    private SalesOrderRepository salesOrderRepository;
    private Gson converter = new Gson();

    public ShowSalesOrderItemsRoute(SalesOrderRepository salesOrderRepository) {
        this.salesOrderRepository = salesOrderRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return converter.toJson(getSalesOrderItems(request));
    }

    private ArrayList<OrderItemInfo> getSalesOrderItems(Request request) {
        ArrayList<OrderItemInfo> itemInfos = new ArrayList<>();
        for (OrderItem item : salesOrderRepository.getById(request.queryString()).items) {
            OrderItemInfo itemInfo = new OrderItemInfo();
            itemInfo.productName = item.productInfo.name;
            itemInfo.quantity = item.quantity;
            itemInfos.add(itemInfo);
        }
        return itemInfos;
    }
}
