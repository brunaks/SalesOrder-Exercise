package TestDoubles.Persistence;

import Entities.Order.PurchaseOrderInfo;
import Interfaces.Persistence.PurchaseOrderRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class InMemoryPurchaseOrderRepository implements PurchaseOrderRepository {

    private List<PurchaseOrderInfo> orders = new ArrayList<PurchaseOrderInfo>();

    @Override
    public List<PurchaseOrderInfo> getAll() {
        return orders;
    }

    @Override
    public void save(PurchaseOrderInfo purchaseOrderInfo) {
        this.addInProcessStatusToOrder(purchaseOrderInfo);
        this.orders.add(purchaseOrderInfo);
    }

    private void addInProcessStatusToOrder(PurchaseOrderInfo purchaseOrderInfo) {
        purchaseOrderInfo.status = purchaseOrderInfo.IN_PROCESS;
    }
}
