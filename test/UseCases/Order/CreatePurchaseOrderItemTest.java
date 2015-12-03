package UseCases.Order;

import Entities.Customer.CustomerInfo;
import Entities.Order.PurchaseOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.PurchaseOrderRepository;
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
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public class CreatePurchaseOrderItemTest {

    private PurchaseOrderRepository purchaseRepository;
    private ProductRepository productRepository;
    private FakePurchaseOrderReceiver orderReceiver;
    private CreatePurchaseOrderUseCase createOrder;
    private AddPurchaseOrderItemUseCase addItem;
    private FakeProductReceiver productReceiver;

    @Before
    public void setUp() throws Exception {
        this.purchaseRepository = new InMemoryPurchaseOrderRepository();
        this.productRepository = new InMemoryProductRepository();
        this.orderReceiver = new FakePurchaseOrderReceiver();
        this.productReceiver = new FakeProductReceiver();
    }

    @Test
    public void canAddOneItem_andCloseOrder() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");

        createOrder = new CreatePurchaseOrderUseCase(orderId, purchaseRepository, productRepository, orderReceiver, date);
        createOrder.execute();

        this.addItem = new AddPurchaseOrderItemUseCase(orderId, purchaseRepository, productRepository, orderReceiver);

        ProductInfo productInfo = givenProductInfo("Name", "Description", 20.0, 10);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(productReceiver, productInfo, productRepository);
        registerProduct.execute();

        addItem.withProductIdAndQuantity(productInfo.id, 5);
        addItem.setOrderToProcessing();

        ReadPurchaseOrderUseCase readOrder = new ReadPurchaseOrderUseCase(purchaseRepository);
        PurchaseOrderInfo info = readOrder.withId(orderId);

        Assert.assertNotNull(info);
        Assert.assertEquals(orderId, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(PurchaseOrderInfo.IN_PROCESS, info.status);
        Assert.assertEquals(20.0 * 5, info.total, 0.01);
        Assert.assertEquals(1, info.items.size());
        Assert.assertFalse(this.orderReceiver.createOrderFailed);
        Assert.assertFalse(this.orderReceiver.orderIdIsInvalid);
        Assert.assertFalse(this.orderReceiver.addItemFailed);
    }

    @Test
    public void canAddTwoItems_andCloseOrder() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");

        createOrder = new CreatePurchaseOrderUseCase(orderId, purchaseRepository, productRepository, orderReceiver, date);
        createOrder.execute();

        this.addItem = new AddPurchaseOrderItemUseCase(orderId, purchaseRepository, productRepository, orderReceiver);

        ProductInfo productInfo = givenProductInfo("Name", "Description", 20.0, 10);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(productReceiver, productInfo, productRepository);
        registerProduct.execute();

        ProductInfo productInfo2 = givenProductInfo("Name2", "Description", 30.0, 5);
        RegisterProductUseCase registerProduct2 = new RegisterProductUseCase(productReceiver, productInfo2, productRepository);
        registerProduct2.execute();

        addItem.withProductIdAndQuantity(productInfo.id, 5);
        addItem.withProductIdAndQuantity(productInfo2.id, 5);
        addItem.setOrderToProcessing();

        ReadPurchaseOrderUseCase readOrder = new ReadPurchaseOrderUseCase(purchaseRepository);
        PurchaseOrderInfo info = readOrder.withId(orderId);

        Assert.assertNotNull(info);
        Assert.assertEquals(orderId, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(PurchaseOrderInfo.IN_PROCESS, info.status);
        Assert.assertEquals((20.0 * 5) + (30.0 * 5), info.total, 0.01);
        Assert.assertEquals(2, info.items.size());
        Assert.assertFalse(this.orderReceiver.createOrderFailed);
        Assert.assertFalse(this.orderReceiver.orderIdIsInvalid);
        Assert.assertFalse(this.orderReceiver.addItemFailed);
    }

    @Test
    public void cannotAddItem_InvalidProductId_orderRemainsOpenIfThereAreNoItems() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");

        createOrder = new CreatePurchaseOrderUseCase(orderId, purchaseRepository, productRepository, orderReceiver, date);
        createOrder.execute();

        this.addItem = new AddPurchaseOrderItemUseCase(orderId, purchaseRepository, productRepository, orderReceiver);
        addItem.withProductIdAndQuantity(UUID.randomUUID().toString(), 5);
        addItem.setOrderToProcessing();

        ReadPurchaseOrderUseCase readOrder = new ReadPurchaseOrderUseCase(purchaseRepository);
        PurchaseOrderInfo info = readOrder.withId(orderId);

        Assert.assertNotNull(info);
        Assert.assertEquals(orderId, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(PurchaseOrderInfo.OPEN, info.status);
        Assert.assertEquals(0.0, info.total, 0.01);
        Assert.assertEquals(0, info.items.size());
        Assert.assertFalse(this.orderReceiver.createOrderFailed);
        Assert.assertFalse(this.orderReceiver.orderIdIsInvalid);
        Assert.assertTrue(this.orderReceiver.addItemFailed);
    }

    @Test
    public void cannotAddItem_OrderIdIsInvalid() {
        String orderId = UUID.randomUUID().toString();
        ProductInfo productInfo = givenProductInfo("Name", "Description", 20.0, 10);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(productReceiver, productInfo, productRepository);
        registerProduct.execute();

        this.addItem = new AddPurchaseOrderItemUseCase(orderId, purchaseRepository, productRepository, orderReceiver);
        addItem.withProductIdAndQuantity(productInfo.id, 5);
        addItem.setOrderToProcessing();

        ReadPurchaseOrderUseCase readOrder = new ReadPurchaseOrderUseCase(purchaseRepository);
        PurchaseOrderInfo info = readOrder.withId(orderId);

        Assert.assertNull(info);
        Assert.assertTrue(this.orderReceiver.addItemFailed);
    }

    @Test
    public void cannotAddTwoItemsWithSameProductId() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");

        createOrder = new CreatePurchaseOrderUseCase(orderId, purchaseRepository, productRepository, orderReceiver, date);
        createOrder.execute();

        this.addItem = new AddPurchaseOrderItemUseCase(orderId, purchaseRepository, productRepository, orderReceiver);

        ProductInfo productInfo = givenProductInfo("Name", "Description", 20.0, 10);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(productReceiver, productInfo, productRepository);
        registerProduct.execute();

        addItem.withProductIdAndQuantity(productInfo.id, 5);
        Assert.assertFalse(this.orderReceiver.addItemFailed);
        addItem.withProductIdAndQuantity(productInfo.id, 5);
        Assert.assertTrue(this.orderReceiver.addItemFailed);
        addItem.setOrderToProcessing();

        ReadPurchaseOrderUseCase readOrder = new ReadPurchaseOrderUseCase(purchaseRepository);
        PurchaseOrderInfo info = readOrder.withId(orderId);

        Assert.assertNotNull(info);
        Assert.assertEquals(orderId, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(PurchaseOrderInfo.IN_PROCESS, info.status);
        Assert.assertEquals((20.0 * 5), info.total, 0.01);
        Assert.assertEquals(1, info.items.size());
        Assert.assertFalse(this.orderReceiver.createOrderFailed);
        Assert.assertFalse(this.orderReceiver.orderIdIsInvalid);
    }

    @Test
    public void cannotAddItemWithNoSufficientUnitsInStock() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");

        createOrder = new CreatePurchaseOrderUseCase(orderId, purchaseRepository, productRepository, orderReceiver, date);
        createOrder.execute();

        this.addItem = new AddPurchaseOrderItemUseCase(orderId, purchaseRepository, productRepository, orderReceiver);

        ProductInfo productInfo = givenProductInfo("Name", "Description", 20.0, 10);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(productReceiver, productInfo, productRepository);
        registerProduct.execute();

        addItem.withProductIdAndQuantity(productInfo.id, 15);
        Assert.assertTrue(this.orderReceiver.addItemFailed);
        addItem.setOrderToProcessing();

        ReadPurchaseOrderUseCase readOrder = new ReadPurchaseOrderUseCase(purchaseRepository);
        PurchaseOrderInfo info = readOrder.withId(orderId);

        Assert.assertNotNull(info);
        Assert.assertEquals(orderId, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(PurchaseOrderInfo.OPEN, info.status);
        Assert.assertEquals(0.0, info.total, 0.01);
        Assert.assertEquals(0, info.items.size());
        Assert.assertFalse(this.orderReceiver.createOrderFailed);
        Assert.assertFalse(this.orderReceiver.orderIdIsInvalid);
    }

    @Test
    public void cannotAddItemToOrderWithStatusDifferentFromOpen() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");

        createOrder = new CreatePurchaseOrderUseCase(orderId, purchaseRepository, productRepository, orderReceiver, date);
        createOrder.execute();

        this.addItem = new AddPurchaseOrderItemUseCase(orderId, purchaseRepository, productRepository, orderReceiver);

        ProductInfo productInfo = givenProductInfo("Name", "Description", 20.0, 10);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(productReceiver, productInfo, productRepository);
        registerProduct.execute();

        ProductInfo productInfo2 = givenProductInfo("Name2", "Description2", 30.0, 10);
        RegisterProductUseCase registerProduct2 = new RegisterProductUseCase(productReceiver, productInfo2, productRepository);
        registerProduct2.execute();

        addItem.withProductIdAndQuantity(productInfo.id, 5);
        Assert.assertFalse(this.orderReceiver.addItemFailed);
        addItem.setOrderToProcessing();

        addItem.withProductIdAndQuantity(productInfo2.id, 8);
        Assert.assertTrue(this.orderReceiver.addItemFailed);

        ReadPurchaseOrderUseCase readOrder = new ReadPurchaseOrderUseCase(purchaseRepository);
        PurchaseOrderInfo info = readOrder.withId(orderId);

        Assert.assertNotNull(info);
        Assert.assertEquals(orderId, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(PurchaseOrderInfo.IN_PROCESS, info.status);
        Assert.assertEquals((20.0 * 5), info.total, 0.01);
        Assert.assertEquals(1, info.items.size());
        Assert.assertFalse(this.orderReceiver.createOrderFailed);
        Assert.assertFalse(this.orderReceiver.orderIdIsInvalid);
    }

    private ProductInfo givenProductInfo(String name, String description, double price, int unitsIsStock) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.id = UUID.randomUUID().toString();
        productInfo.description = description;
        productInfo.name = name;
        productInfo.price = price;
        productInfo.unitsInStock = unitsIsStock;
        return productInfo;
    }

    private CustomerInfo givenCustomerInfo(String name, String cpf, String telephoneNumber, String address) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.id = UUID.randomUUID().toString();
        customerInfo.name = name;
        customerInfo.cpf = cpf;
        customerInfo.telephoneNumber = telephoneNumber;
        customerInfo.address = address;
        return customerInfo;
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
