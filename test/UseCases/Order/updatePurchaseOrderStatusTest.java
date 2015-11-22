package UseCases.Order;

import Entities.Order.PurchaseOrderInfo;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Persistence.InMemoryPurchaseOrderRepository;
import TestDoubles.Receiver.FakeProductReceiver;
import TestDoubles.Receiver.FakePurchaseOrderReceiver;
import UseCases.Product.RegisterProductUseCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 22/11/2015.
 */
public class updatePurchaseOrderStatusTest {

    private InMemoryPurchaseOrderRepository orderRepository;
    private InMemoryProductRepository productRepository;
    private FakePurchaseOrderReceiver receiver;
    CreatePurchaseOrderUseCase createOrder;
    ListPurchaseOrdersUseCase listOrders;

    @Before
    public void setUp() throws Exception {
        orderRepository = new InMemoryPurchaseOrderRepository();
        productRepository = new InMemoryProductRepository();
        receiver = new FakePurchaseOrderReceiver();
    }

    @Test
    public void canChangeStatusFromInProcessToDelivered() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createOrder = new CreatePurchaseOrderUseCase(id, orderRepository, productRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name", "Description", 10, 10);
        createOrder.addProduct(productInfo.id, 1);
        createOrder.execute();
        Assert.assertFalse(receiver.orderFailed);

        UpdatePurchaseOrderStatusUseCase updateOrderStatus = new UpdatePurchaseOrderStatusUseCase(id, orderRepository, receiver);
        updateOrderStatus.changeTo(PurchaseOrderInfo.DELIVERED);

        listOrders = new ListPurchaseOrdersUseCase(orderRepository);
        List<PurchaseOrderInfo> orders = listOrders.getAll();
        Assert.assertEquals(SalesOrderInfo.DELIVERED, orders.get(0).status);
    }

    @Test
    public void cannotChangeStatusFromInProcessToDelivered_OrderIdIsInvalid() {
        String id = UUID.randomUUID().toString();
        UpdatePurchaseOrderStatusUseCase updateOrderStatus = new UpdatePurchaseOrderStatusUseCase(id, orderRepository, receiver);
        updateOrderStatus.changeTo(PurchaseOrderInfo.DELIVERED);
        Assert.assertTrue(receiver.statusUpdateFailed);
        Assert.assertTrue(receiver.orderIdIsInvalid);
    }

    private Date givenDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

    private ProductInfo givenProductInfo(String name, String description, int unitsInStock, double unitPrice) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.name = name;
        productInfo.description = description;
        productInfo.unitsInStock = unitsInStock;
        productInfo.price = unitPrice;
        productInfo.id = productRepository.createProductInfoID();
        RegisterProductUseCase registerProductUseCase = new RegisterProductUseCase(new FakeProductReceiver(), productInfo, productRepository);
        registerProductUseCase.execute();
        return productInfo;
    }
}
