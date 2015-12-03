package Routes.Order.PurchaseOrder;

import Entities.Order.PurchaseOrderInfo;
import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Persistence.PurchaseOrderRepository;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 03/12/2015.
 */
public class DisplayPurchaseOrderRoute implements Route {

    private PurchaseOrderRepository purchaseOrderRepository;
    private Gson converter = new Gson();

    public DisplayPurchaseOrderRoute(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return converter.toJson(makePurchaseOrderInfo(request));
    }

    private PurchaseOrderInfo makePurchaseOrderInfo(Request request) {
        String requestString = request.queryString();
        return purchaseOrderRepository.getById(request.queryString());
    }
}
