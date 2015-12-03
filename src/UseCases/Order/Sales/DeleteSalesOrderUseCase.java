package UseCases.Order.Sales;

import Entities.Order.SalesOrderInfo;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.SalesOrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class DeleteSalesOrderUseCase {

    private SalesOrderReceiver salesOrderReceiver;
    private SalesOrderRepository salesOrderRepository;

    public DeleteSalesOrderUseCase(SalesOrderRepository salesOrderRepository, SalesOrderReceiver receiver) {
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderReceiver = receiver;
    }

    public void executeWithSalesOrderID(String id) {
        SalesOrderInfo salesOrderInfo = this.salesOrderRepository.getById(id);
        if (salesOrderInfo != null) {
            deleteSalesOrder(id);
        } else {
            this.salesOrderReceiver.salesOrderIdIsInvalid();
            this.salesOrderReceiver.deleteFailed();
        }
    }

    private void deleteSalesOrder(String id) {
        this.salesOrderRepository.deleteWithId(id);
    }
}
