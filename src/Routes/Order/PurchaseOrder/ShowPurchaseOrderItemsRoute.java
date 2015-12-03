package Routes.Order.PurchaseOrder;

import Entities.Order.OrderItem;
import Entities.Order.PurchaseOrderInfo;
import Interfaces.Persistence.PurchaseOrderRepository;
import Routes.RequestObjects.OrderItemInfo;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;

/**
 * Created by Bruna Koch Schmitt on 03/12/2015.
 */
public class ShowPurchaseOrderItemsRoute implements Route {
    private PurchaseOrderRepository purchaseOrderRepository;
    private Gson converter = new Gson();

    public ShowPurchaseOrderItemsRoute(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return converter.toJson(getPurchaseOrderItems(request));
    }

    private ArrayList<OrderItemInfo> getPurchaseOrderItems(Request request) {
        ArrayList<OrderItemInfo> itemInfos = new ArrayList<>();
        PurchaseOrderInfo purchaseOrderInfo = purchaseOrderRepository.getById(request.queryString());
        if (purchaseOrderInfo != null) {
            for (OrderItem item :purchaseOrderInfo.items) {
                OrderItemInfo itemInfo = new OrderItemInfo();
                itemInfo.productName = item.productInfo.name;
                itemInfo.quantity = item.quantity;
                itemInfos.add(itemInfo);
            }
        }
        return itemInfos;
    }
}
