package UseCases.Order;

import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Receivers.PurchaseOrderReceiver;

import java.util.Date;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class CreatePurchaseOrderUseCase {
    public CreatePurchaseOrderUseCase(String id, PurchaseOrderRepository purchaseOrderRepository, ProductRepository productRepository, PurchaseOrderReceiver receiver, Date date) {

    }

    public void addProduct(String id, int quantity) {

    }

    public void execute() {

    }

    public double getTotal() {
        return 10;
    }
}
