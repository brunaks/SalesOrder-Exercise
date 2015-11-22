package UseCases.Order;

import Entities.Order.PurchaseOrderInfo;
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
 * Created by Bruna Koch Schmitt on 21/11/2015.
 */
public class DeletePurchaseOrderTest {

    FakePurchaseOrderReceiver receiver;
    InMemoryPurchaseOrderRepository purchaseOrderRepository;
    CreatePurchaseOrderUseCase createOrder;
    InMemoryProductRepository productRepository;

    @Before
    public void setUp() throws Exception {
        receiver = new FakePurchaseOrderReceiver();
        purchaseOrderRepository = new InMemoryPurchaseOrderRepository();
        productRepository = new InMemoryProductRepository();
    }

    @Test
    public void canDeleteOrderSuccessfully() {
        PurchaseOrderInfo purchaseOrderInfo = givenValidOrder();
        DeletePurchaseOrderUseCase deleteOrder = new DeletePurchaseOrderUseCase(purchaseOrderRepository, receiver);
        deleteOrder.executeWithOrderID(purchaseOrderInfo.id);
        this.assertOrderWasDeleted();
    }

    @Test
    public void cannotDeleteSalesOrder_IdIsInvalid_OrderDoesNotExist() {
        DeletePurchaseOrderUseCase deleteOrder = new DeletePurchaseOrderUseCase(purchaseOrderRepository, receiver);
        deleteOrder.executeWithOrderID(UUID.randomUUID().toString());
        Assert.assertTrue(receiver.orderIdIsInvalid);
        Assert.assertTrue(receiver.deleteFailed);
    }

    private void assertOrderWasDeleted() {
        ListPurchaseOrdersUseCase listPurchaseOrdersUseCase = new ListPurchaseOrdersUseCase(purchaseOrderRepository);
        List<PurchaseOrderInfo> orderInfos = listPurchaseOrdersUseCase.getAll();
        Assert.assertEquals(0, orderInfos.size());

        Assert.assertFalse(this.receiver.deleteFailed);
        Assert.assertFalse(this.receiver.orderIdIsInvalid);
    }

    private PurchaseOrderInfo givenValidOrder() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createOrder = new CreatePurchaseOrderUseCase(id, purchaseOrderRepository, productRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name", "Description", 10, 10);
        createOrder.addProduct(productInfo.id, 1);
        createOrder.execute();
        Assert.assertFalse(receiver.orderFailed);
        assertTotalEquals(10.0);
        ListPurchaseOrdersUseCase listOrdersUseCase = new ListPurchaseOrdersUseCase(purchaseOrderRepository);
        List<PurchaseOrderInfo> orderInfos = listOrdersUseCase.getAll();
        return orderInfos.get(0);
    }

    private void assertTotalEquals(double expectedTotal) {
        Assert.assertEquals(expectedTotal, createOrder.getTotal(), 0.01);
    }

    private ProductInfo givenProductInfo(String name, String description, int unitsInStock, int unitPrice) {
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
}
