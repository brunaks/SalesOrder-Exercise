package UseCases.Order.Sales;

import Entities.Order.SalesOrderInfo;
import Interfaces.Persistence.CustomerRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.SalesOrderReceiver;

import java.util.Date;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public class CreateSalesOrderUseCase {

    private String customerId;
    private Date date;
    private String id;
    private CustomerRepository customerRepository;
    private SalesOrderRepository salesOrderRepository;
    private SalesOrderReceiver receiver;
    private SalesOrderInfo salesOrderInfo;

    public CreateSalesOrderUseCase(String id, String customerId, SalesOrderRepository salesOrderRepository, CustomerRepository customerRepository, SalesOrderReceiver receiver, Date date) {
        this.salesOrderRepository = salesOrderRepository;
        this.customerRepository = customerRepository;
        this.receiver = receiver;
        this.id = id;
        this.date = date;
        this.customerId = customerId;
    }

    public void execute() {
        if (customerRepository.getCustomerById(customerId) == null) {
            receiver.createOrderFailed();
            receiver.customerDoesNotExist();
        } else {
            SalesOrderInfo salesOrderInfo = this.createSalesOrderInfoToSave();
            this.salesOrderRepository.save(salesOrderInfo);
        }
    }

    private SalesOrderInfo createSalesOrderInfoToSave() {
        this.salesOrderInfo = new SalesOrderInfo();
        this.salesOrderInfo.id = this.id;
        this.salesOrderInfo.date = this.date;
        this.salesOrderInfo.customerId = this.customerId;
        this.salesOrderInfo.status = SalesOrderInfo.OPEN;
        return salesOrderInfo;
    }
}
