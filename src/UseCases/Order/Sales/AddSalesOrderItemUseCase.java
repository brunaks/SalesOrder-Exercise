package UseCases.Order.Sales;

import Entities.Order.OrderItem;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.SalesOrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 30/11/2015.
 */
public class AddSalesOrderItemUseCase {

    private SalesOrderReceiver receiver;
    private ProductRepository productRepository;
    private SalesOrderRepository repository;
    private String orderId;

    public AddSalesOrderItemUseCase(String orderId, SalesOrderRepository salesRepository, ProductRepository productRepository, SalesOrderReceiver orderReceiver) {
        this.orderId = orderId;
        this.repository = salesRepository;
        this.productRepository = productRepository;
        this.receiver = orderReceiver;
    }

    public void withProductIdAndQuantity(String productId, int quantity) {
        SalesOrderInfo order = this.repository.getById(this.orderId);
        if (order != null) {
            ProductInfo productInfo = this.productRepository.getProductInfoById(productId);
            if (productInfo != null) {
                if (!this.productWasAlreadyAddedAsItem(order, productId) &&
                        productInfo.unitsInStock >= quantity &&
                        order.status.equalsIgnoreCase(SalesOrderInfo.OPEN)) {
                    OrderItem item = new OrderItem(productInfo, quantity);
                    order.total = order.total + productInfo.price * quantity;
                    order.items.add(item);
                    this.repository.addItem(order, item);
                    this.receiver.addItemWasSuccessful();
                } else {
                    this.receiver.addItemFailed();
                }
            } else {
                this.receiver.addItemFailed();
            }
        } else {
            this.receiver.addItemFailed();
        }
    }

    private boolean productWasAlreadyAddedAsItem(SalesOrderInfo order, String productId) {
        for (OrderItem item : order.items) {
            if (item.productInfo.id.equals(productId)) {
                return true;
            }
        }
        return false;
    }

    public void setOrderToProcessing() {
        SalesOrderInfo order = this.repository.getById(this.orderId);
        if (order != null && order.items.size() > 0) {
            order.status = SalesOrderInfo.IN_PROCESS;
            //this.repository.save(order);
            UpdateSalesOrderStatusUseCase updateStatus = new UpdateSalesOrderStatusUseCase(order.id, this.repository, this.receiver);
            updateStatus.changeTo(SalesOrderInfo.IN_PROCESS);
        }
    }
}
