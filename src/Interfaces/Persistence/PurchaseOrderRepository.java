package Interfaces.Persistence;

import Entities.Order.OrderItem;
import Entities.Order.PurchaseOrderInfo;

import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public interface PurchaseOrderRepository {

    List<PurchaseOrderInfo> getAll();
    void save(PurchaseOrderInfo purchaseOrderInfo);
    PurchaseOrderInfo getById(String id);
    void removeWithId(String id);
    void updateStatus(String id, String newStatus);

    void addItem(PurchaseOrderInfo order, OrderItem item);
}
