package UseCases.Order;

import Entities.Order.OrderItem;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;

/**
 * Created by Bruna Koch Schmitt on 30/11/2015.
 */
public class AddSalesOrderItemUseCase {

    private ProductRepository productRepository;
    private SalesOrderRepository repository;
    private String orderId;

    public AddSalesOrderItemUseCase(String orderId, SalesOrderRepository salesRepository, ProductRepository productRepository) {
        this.orderId = orderId;
        this.repository = salesRepository;
        this.productRepository = productRepository;
    }

    public void withProductIdAndQuantity(String productId, int quantity) {
        SalesOrderInfo order = this.repository.getById(this.orderId);
        if (order != null) {
            ProductInfo productInfo = this.productRepository.getProductInfoById(productId);
            if (productInfo != null) {
                OrderItem item = new OrderItem(productInfo, quantity);
                order.total = order.total + productInfo.price * quantity;
                order.items.add(item);
                this.repository.save(order);
            }
        }
    }

    public void closeOrder() {
        SalesOrderInfo order = this.repository.getById(this.orderId);
        if (order != null) {
            order.status = SalesOrderInfo.IN_PROCESS;
            this.repository.save(order);
        }
    }
}
