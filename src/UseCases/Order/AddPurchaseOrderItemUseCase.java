package UseCases.Order;

import Entities.Order.OrderItem;
import Entities.Order.PurchaseOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Receivers.PurchaseOrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public class AddPurchaseOrderItemUseCase {
    private PurchaseOrderReceiver receiver;
    private ProductRepository productRepository;
    private PurchaseOrderRepository repository;
    private String orderId;

    public AddPurchaseOrderItemUseCase(String orderId, PurchaseOrderRepository purchaseRepository, ProductRepository productRepository, PurchaseOrderReceiver orderReceiver) {
        this.orderId = orderId;
        this.repository = purchaseRepository;
        this.productRepository = productRepository;
        this.receiver = orderReceiver;
    }

    public void withProductIdAndQuantity(String productId, int quantity) {
        PurchaseOrderInfo order = this.repository.getById(this.orderId);
        if (order != null) {
            ProductInfo productInfo = this.productRepository.getProductInfoById(productId);
            if (productInfo != null) {
                if (!this.productWasAlreadyAddedAsItem(order, productId) &&
                        productInfo.unitsInStock >= quantity &&
                        order.status.equalsIgnoreCase(PurchaseOrderInfo.OPEN)) {
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

    private boolean productWasAlreadyAddedAsItem(PurchaseOrderInfo order, String productId) {
        for (OrderItem item : order.items) {
            if (item.productInfo.id.equals(productId)) {
                return true;
            }
        }
        return false;
    }

    public void setOrderToProcessing() {
        PurchaseOrderInfo order = this.repository.getById(this.orderId);
        if (order != null && order.items.size() > 0) {
            order.status = PurchaseOrderInfo.IN_PROCESS;
            //this.repository.save(order);
            UpdatePurchaseOrderStatusUseCase updateStatus = new UpdatePurchaseOrderStatusUseCase(order.id, this.repository, this.receiver);
            updateStatus.changeTo(PurchaseOrderInfo.IN_PROCESS);
        }
    }
}
