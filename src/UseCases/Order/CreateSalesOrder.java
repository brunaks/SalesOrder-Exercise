package UseCases.Order;

import Entities.Customer.CustomerInfo;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.OrderReceiver;

import java.util.Date;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public class CreateSalesOrder {

    public CreateSalesOrder(SalesOrderRepository repository, OrderReceiver receiver, SalesOrderInfo orderInfo) {

    }

    public void addProduct(String productInfo, int i, Date date) {

    }

    public void addCustomer(CustomerInfo customerInfo) {

    }

    public double getTotal() {
        return 10;
    }
}
