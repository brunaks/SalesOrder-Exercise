package UseCases.Order;

import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.SalesOrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 22/11/2015.
 */
public class UpdateSalesOrderStatusUseCase {

    private SalesOrderReceiver receiver;
    private String id;
    private SalesOrderRepository salesOrderRepository;

    public UpdateSalesOrderStatusUseCase(String id, SalesOrderRepository salesOrderRepository, SalesOrderReceiver receiver) {
        this.salesOrderRepository = salesOrderRepository;
        this.id = id;
        this.receiver = receiver;
    }

    public void changeTo(String newStatus) {
        if (this.salesOrderRepository.getById(id) != null) {
            this.salesOrderRepository.updateStatus(id, newStatus);
        } else {
            this.receiver.salesOrderIdIsInvalid();
            this.receiver.updateStatusFailed();
        }
    }
}
