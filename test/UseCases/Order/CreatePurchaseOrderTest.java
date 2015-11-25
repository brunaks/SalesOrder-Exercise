package UseCases.Order;

import Entities.Order.PurchaseOrderInfo;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import TestDoubles.Persistence.InMemoryCustomerRepository;
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
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class CreatePurchaseOrderTest {

    FakePurchaseOrderReceiver receiver;
    InMemoryPurchaseOrderRepository purchaseOrderRepository;
    CreatePurchaseOrderUseCase createPurchaseOrder;
    InMemoryProductRepository productRepository;
    InMemoryCustomerRepository customerRepository;

    @Before
    public void setUp() throws Exception {
        receiver = new FakePurchaseOrderReceiver();
        purchaseOrderRepository = new InMemoryPurchaseOrderRepository();
        productRepository = new InMemoryProductRepository();
        customerRepository = new InMemoryCustomerRepository();
    }

    @Test
    public void canCreateOrderWithSuccess_OneProduct() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createPurchaseOrder = new CreatePurchaseOrderUseCase(id, purchaseOrderRepository, productRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name", "Description", 10, 10);
        createPurchaseOrder.addProduct(productInfo.id, 1);
        createPurchaseOrder.execute();
        Assert.assertFalse(receiver.orderFailed);
        assertTotalEquals(10.0);
    }

    @Test
    public void canCreateOrderWithSucess_TwoProducts() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createPurchaseOrder = new CreatePurchaseOrderUseCase(id, purchaseOrderRepository, productRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name1", "Description1", 10, 10);
        ProductInfo productInfo2 = givenProductInfo("Name2", "Description2", 20, 20);
        createPurchaseOrder.addProduct(productInfo.id, 1);
        createPurchaseOrder.addProduct(productInfo2.id, 2);
        createPurchaseOrder.execute();
        Assert.assertFalse(receiver.orderFailed);
        assertTotalEquals(50.0);
    }

    @Test
    public void createOrderFailed_productIdDoesNotExist() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createPurchaseOrder = new CreatePurchaseOrderUseCase(id, purchaseOrderRepository, productRepository, receiver, date);
        createPurchaseOrder.addProduct(UUID.randomUUID().toString(), 1);
        Assert.assertTrue(receiver.orderFailed);
        Assert.assertTrue(receiver.productDoesNotExist);
        assertTotalEquals(0.0);
    }

    @Test
    public void createOrderSuccessful_canRetrievePurchaseOrder() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createPurchaseOrder = new CreatePurchaseOrderUseCase(id, purchaseOrderRepository, productRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name1", "Description1", 10, 10);
        createPurchaseOrder.addProduct(productInfo.id, 1);
        createPurchaseOrder.execute();
        Assert.assertFalse(receiver.orderFailed);
        Assert.assertFalse(receiver.productDoesNotExist);
        assertTotalEquals(10.0);
        ListPurchaseOrdersUseCase listPurchaseOrders = new ListPurchaseOrdersUseCase(purchaseOrderRepository);
        List<PurchaseOrderInfo> purchaseOrderInfos = listPurchaseOrders.getAll();

        Assert.assertEquals(id, purchaseOrderInfos.get(0).id);
        Assert.assertEquals(date, purchaseOrderInfos.get(0).date);
        Assert.assertEquals(SalesOrderInfo.IN_PROCESS, purchaseOrderInfos.get(0).status);
    }

    private void assertTotalEquals(Double expectedTotal) {
        Assert.assertEquals(expectedTotal, createPurchaseOrder.getTotal(), 0.01);
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
