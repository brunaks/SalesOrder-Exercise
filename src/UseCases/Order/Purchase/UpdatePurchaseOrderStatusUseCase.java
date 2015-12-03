package UseCases.Order.Purchase;

import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Receivers.PurchaseOrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 22/11/2015.
 */
public class UpdatePurchaseOrderStatusUseCase {

    private String id;
    private PurchaseOrderRepository orderRepository;
    private PurchaseOrderReceiver receiver;

    public UpdatePurchaseOrderStatusUseCase(String id, PurchaseOrderRepository orderRepository, PurchaseOrderReceiver receiver) {
        this.id = id;
        this.orderRepository = orderRepository;
        this.receiver = receiver;
    }

    public void changeTo(String newStatus) {
        if (this.orderRepository.getById(this.id) != null) {
            this.orderRepository.updateStatus(this.id, newStatus);
        } else {
            this.receiver.updateStatusFailed();
            this.receiver.orderIdIsInvalid();
        }
    }
}
