package UseCases.Order;

import Entities.Order.PurchaseOrderInfo;
import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Receivers.PurchaseOrderReceiver;
import TestDoubles.Persistence.InMemoryPurchaseOrderRepository;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public class ReadPurchaseOrderUseCase {
    private PurchaseOrderRepository repository;

    public ReadPurchaseOrderUseCase(PurchaseOrderRepository purchaseOrderRepository) {
        this.repository = purchaseOrderRepository;
    }

    public PurchaseOrderInfo withId(String id) {
        return this.repository.getById(id);
    }
}
