package UseCases.Order;

import Entities.Customer.CustomerInfo;
import Entities.Order.SalesOrderItem;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.OrderReceiver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public class CreateSalesOrder {

    private OrderReceiver receiver;
    private List<SalesOrderItem> items = new ArrayList<SalesOrderItem>();
    SalesOrderRepository salesOrderRepository;
    ProductRepository productRepository;

    public CreateSalesOrder(SalesOrderRepository salesOrderRepository, ProductRepository productRepository, OrderReceiver receiver, Date date) {
        this.salesOrderRepository = salesOrderRepository;
        this.productRepository = productRepository;
        this.receiver = receiver;
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

    public void addCustomer(CustomerInfo customerInfo) {

    }

    public double getTotal() {
        return calculateTotalValueOfOrder();
    }

    private double calculateTotalValueOfOrder() {
        Double total = 0.0;
        for (SalesOrderItem item: items) {
            total += item.getProductInfo().price*item.getQuantity();
        }
        return total;
    }
}
