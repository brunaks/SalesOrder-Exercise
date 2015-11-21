package UseCases.Order;

import Entities.Order.PurchaseOrderInfo;
import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Receivers.PurchaseOrderReceiver;
import TestDoubles.Persistence.InMemoryPurchaseOrderRepository;
import TestDoubles.Receiver.FakePurchaseOrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 21/11/2015.
 */
public class DeletePurchaseOrderUseCase {

    private final PurchaseOrderReceiver receiver;
    private PurchaseOrderRepository repository;

    public DeletePurchaseOrderUseCase(PurchaseOrderRepository purchaseOrderRepository, FakePurchaseOrderReceiver receiver) {
        this.repository = purchaseOrderRepository;
        this.receiver = receiver;
    }

    public void executeWithOrderID(String id) {
        PurchaseOrderInfo infoToBeDeleted = this.repository.getById(id);
        if (infoToBeDeleted != null) {
            this.repository.removeWithId(id);
        }
    }
}
