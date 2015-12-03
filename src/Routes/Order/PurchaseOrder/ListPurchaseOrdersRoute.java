package Routes.Order.PurchaseOrder;

import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Receivers.PurchaseOrderReceiver;
import Interfaces.Receivers.PurchaseOrderReceiver;
import UseCases.Order.ListPurchaseOrdersUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 03/12/2015.
 */
public class ListPurchaseOrdersRoute implements Route {

    private PurchaseOrderReceiver receiver;
    private PurchaseOrderRepository repository;

    public ListPurchaseOrdersRoute(PurchaseOrderRepository purchaseOrderRepository, PurchaseOrderReceiver purchaseOrderReceiver) {
        this.repository = purchaseOrderRepository;
        this.receiver = purchaseOrderReceiver;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ListPurchaseOrdersUseCase list = new ListPurchaseOrdersUseCase(repository);
        Gson converter = new Gson();
        return converter.toJson(list.getAllSummaries());
    }
}
