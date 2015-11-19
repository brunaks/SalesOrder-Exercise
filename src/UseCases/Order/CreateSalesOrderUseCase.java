package UseCases.Order;

import Entities.Customer.CustomerInfo;
import Entities.Order.SalesOrderInfo;
import Entities.Order.SalesOrderItem;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.CustomerRepository;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.OrderReceiver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public class CreateSalesOrderUseCase {

    private Date date;
    private String id;
    private CustomerRepository customerRepository;
    private SalesOrderRepository salesOrderRepository;
    private ProductRepository productRepository;
    private OrderReceiver receiver;
    private List<SalesOrderItem> items = new ArrayList<SalesOrderItem>();
    private CustomerInfo customerInfo;
    private SalesOrderInfo salesOrderInfo;

    public CreateSalesOrderUseCase(String id, SalesOrderRepository salesOrderRepository, ProductRepository productRepository, CustomerRepository customerRepository, OrderReceiver receiver, Date date) {
        this.salesOrderRepository = salesOrderRepository;
        this.productRepository = productRepository;
        this.receiver = receiver;
        this.customerRepository = customerRepository;
        this.id = id;
        this.date = date;
    }

    public void addProduct(String productInfoID, int quantity) {
        ProductInfo productInfo = productRepository.getProductInfoById(productInfoID);
        if (productInfo != null) {
            items.add(new SalesOrderItem(productInfo, quantity));
        } else {
            this.receiver.createOrderFailed();
            this.receiver.productDoesNotExist();
        }
    }

    public void addCustomer(String customerID) {
        this.customerInfo = customerRepository.getCustomerByID(customerID);
        if (this.customerInfo == null) {
            receiver.createOrderFailed();
            receiver.clientDoesNotExist();
        }
    }

    public double getTotal() {
        return calculateTotalValueOfOrder();
    }

    private double calculateTotalValueOfOrder() {
        Double total = 0.0;
        if (this.customerInfo != null) {
            for (SalesOrderItem item : items) {
                total += item.getProductInfo().price * item.getQuantity();
            }
        }
        return total;
    }

    public void execute() {
        SalesOrderInfo salesOrderInfo = this.createSalesOrderInfoToSave();
        this.salesOrderRepository.save(salesOrderInfo);
    }

    private SalesOrderInfo createSalesOrderInfoToSave() {
        this.salesOrderInfo = new SalesOrderInfo();
        this.salesOrderInfo.id = this.id;
        this.salesOrderInfo.date = this.date;
        this.salesOrderInfo.customerInfo = this.customerInfo;
        this.salesOrderInfo.items = this.items;
        this.salesOrderInfo.total = this.getTotal();
        return salesOrderInfo;
    }
}
