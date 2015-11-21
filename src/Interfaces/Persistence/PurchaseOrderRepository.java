package Interfaces.Persistence;

import Entities.Order.PurchaseOrderInfo;

import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public interface PurchaseOrderRepository {

    List<PurchaseOrderInfo> getAll();
    void save(PurchaseOrderInfo purchaseOrderInfo);
}
