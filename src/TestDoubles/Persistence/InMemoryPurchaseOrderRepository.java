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
        this.orders.add(purchaseOrderInfo);
    }

    @Override
    public PurchaseOrderInfo getById(String id) {
        for (PurchaseOrderInfo order : orders) {
            if (order.id.equals(id)) {
                return order;
            }
        }
        return null;
    }

    @Override
    public void removeWithId(String id) {
        PurchaseOrderInfo orderToBeDeleted = this.getById(id);
        if (orderToBeDeleted != null) {
            this.orders.remove(orderToBeDeleted);
        }
    }

    @Override
    public void updateStatus(String id, String newStatus) {
        this.getById(id).status = newStatus;
    }
}
