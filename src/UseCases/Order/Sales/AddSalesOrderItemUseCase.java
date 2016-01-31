package UseCases.Order.Sales;

import Entities.FinancialRecords.SumToReceiveInfo;
import Entities.Order.OrderItem;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Persistence.SumToReceiveRepository;
import Interfaces.Receivers.SalesOrderReceiver;
import UseCases.Financials.CreateSumToReceiveUseCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 30/11/2015.
 */
public class AddSalesOrderItemUseCase {

    private SalesOrderReceiver receiver;
    private ProductRepository productRepository;
    private SalesOrderRepository repository;
    private String orderId;
    private SumToReceiveRepository sumToReceiveRepository;

    public AddSalesOrderItemUseCase(String orderId, SalesOrderRepository salesRepository, ProductRepository productRepository, SalesOrderReceiver orderReceiver, SumToReceiveRepository sumToReceiveRepository) {
        this.orderId = orderId;
        this.repository = salesRepository;
        this.productRepository = productRepository;
        this.receiver = orderReceiver;
        this.sumToReceiveRepository = sumToReceiveRepository;
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
            UpdateSalesOrderStatusUseCase updateStatus = new UpdateSalesOrderStatusUseCase(order.id, this.repository, this.receiver);
            updateStatus.changeTo(SalesOrderInfo.IN_PROCESS);
            createSumToReceive(order);
        }
    }

    private void createSumToReceive(SalesOrderInfo order) {
        SumToReceiveInfo sumToReceiveInfo = new SumToReceiveInfo();
        sumToReceiveInfo.sumId = UUID.randomUUID().toString();
        sumToReceiveInfo.orderId = order.id;
        sumToReceiveInfo.payStatus = SumToReceiveInfo.OPEN;
        sumToReceiveInfo.payDate = getCurrentDate();
        sumToReceiveInfo.sumWithDeduction = calculateSumWithDeduction(order);
        CreateSumToReceiveUseCase createSumToReceive = new CreateSumToReceiveUseCase(sumToReceiveRepository);
        createSumToReceive.execute(sumToReceiveInfo);
    }

    private Double calculateSumWithDeduction(SalesOrderInfo order) {
        return order.total * 0.97;
    }

    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = formatter.format(new Date());
        return date;
    }
}
