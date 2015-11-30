package UseCases.Order;

import Entities.Customer.CustomerInfo;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.CustomerRepository;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.CustomerReceiver;
import Interfaces.Receivers.ProductReceiver;
import Interfaces.Receivers.SalesOrderReceiver;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Persistence.InMemorySalesOrderRepository;
import TestDoubles.Receiver.FakeCustomerReceiver;
import TestDoubles.Receiver.FakeProductReceiver;
import TestDoubles.Receiver.FakeSalesOrderReceiver;
import UseCases.Customer.RegisterCustomerUseCase;
import UseCases.Product.RegisterProductUseCase;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 29/11/2015.
 */
public class createSalesOrderItemTest {

    private SalesOrderRepository salesRepository;
    private ProductRepository productRepository;
    private CustomerRepository customerRepository;
    private FakeSalesOrderReceiver orderReceiver;
    private CreateSalesOrderUseCase createOrder;
    private FakeCustomerReceiver customerReceiver;
    private AddSalesOrderItemUseCase addItem;
    private FakeProductReceiver productReceiver;

    @Before
    public void setUp() throws Exception {
        this.salesRepository = new InMemorySalesOrderRepository();
        this.productRepository = new InMemoryProductRepository();
        this.customerRepository = new InMemoryCustomerRepository();
        this.orderReceiver = new FakeSalesOrderReceiver();
        this.customerReceiver = new FakeCustomerReceiver();
        this.productReceiver = new FakeProductReceiver();
    }

    @Test
    public void canAddOneItem_andCloseOrder() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");
        CustomerInfo customerInfo = givenCustomer();

        createOrder = new CreateSalesOrderUseCase(orderId, customerInfo.id, salesRepository, customerRepository, orderReceiver, date);
        createOrder.execute();

        this.addItem = new AddSalesOrderItemUseCase(orderId, salesRepository, productRepository, orderReceiver);

        ProductInfo productInfo = givenProductInfo("Name", "Description", 20.0, 10);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(productReceiver, productInfo, productRepository);
        registerProduct.execute();

        addItem.withProductIdAndQuantity(productInfo.id, 5);
        addItem.closeOrder();

        ReadSalesOrderUseCase readOrder = new ReadSalesOrderUseCase(salesRepository);
        SalesOrderInfo info = readOrder.withId(orderId);

        Assert.assertNotNull(info);
        Assert.assertEquals(orderId, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(customerInfo.id, info.customerId);
        Assert.assertEquals(SalesOrderInfo.IN_PROCESS, info.status);
        Assert.assertEquals(20.0 * 5, info.total, 0.01);
        Assert.assertEquals(1, info.items.size());
        Assert.assertFalse(this.orderReceiver.createOrderFailed);
        Assert.assertFalse(this.orderReceiver.salesOrderIdIsInvalid);
        Assert.assertFalse(this.orderReceiver.customerDoesNotExist);
        Assert.assertFalse(this.orderReceiver.addItemFailed);
    }

    @Test
    public void canAddTwoItems_andCloseOrder() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");
        CustomerInfo customerInfo = givenCustomer();

        createOrder = new CreateSalesOrderUseCase(orderId, customerInfo.id, salesRepository, customerRepository, orderReceiver, date);
        createOrder.execute();

        this.addItem = new AddSalesOrderItemUseCase(orderId, salesRepository, productRepository, orderReceiver);

        ProductInfo productInfo = givenProductInfo("Name", "Description", 20.0, 10);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(productReceiver, productInfo, productRepository);
        registerProduct.execute();

        ProductInfo productInfo2 = givenProductInfo("Name2", "Description", 30.0, 5);
        RegisterProductUseCase registerProduct2 = new RegisterProductUseCase(productReceiver, productInfo2, productRepository);
        registerProduct2.execute();

        addItem.withProductIdAndQuantity(productInfo.id, 5);
        addItem.withProductIdAndQuantity(productInfo2.id, 5);
        addItem.closeOrder();

        ReadSalesOrderUseCase readOrder = new ReadSalesOrderUseCase(salesRepository);
        SalesOrderInfo info = readOrder.withId(orderId);

        Assert.assertNotNull(info);
        Assert.assertEquals(orderId, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(customerInfo.id, info.customerId);
        Assert.assertEquals(SalesOrderInfo.IN_PROCESS, info.status);
        Assert.assertEquals((20.0 * 5) + (30.0 * 5), info.total, 0.01);
        Assert.assertEquals(2, info.items.size());
        Assert.assertFalse(this.orderReceiver.createOrderFailed);
        Assert.assertFalse(this.orderReceiver.salesOrderIdIsInvalid);
        Assert.assertFalse(this.orderReceiver.customerDoesNotExist);
        Assert.assertFalse(this.orderReceiver.addItemFailed);
    }

    @Test
    public void cannotAddItem_InvalidProductId_orderRemainsOpenIfThereAreNoItems() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");
        CustomerInfo customerInfo = givenCustomer();

        createOrder = new CreateSalesOrderUseCase(orderId, customerInfo.id, salesRepository, customerRepository, orderReceiver, date);
        createOrder.execute();

        this.addItem = new AddSalesOrderItemUseCase(orderId, salesRepository, productRepository, orderReceiver);
        addItem.withProductIdAndQuantity(UUID.randomUUID().toString(), 5);
        addItem.closeOrder();

        ReadSalesOrderUseCase readOrder = new ReadSalesOrderUseCase(salesRepository);
        SalesOrderInfo info = readOrder.withId(orderId);

        Assert.assertNotNull(info);
        Assert.assertEquals(orderId, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(customerInfo.id, info.customerId);
        Assert.assertEquals(SalesOrderInfo.OPEN, info.status);
        Assert.assertEquals(0.0, info.total, 0.01);
        Assert.assertEquals(0, info.items.size());
        Assert.assertFalse(this.orderReceiver.createOrderFailed);
        Assert.assertFalse(this.orderReceiver.salesOrderIdIsInvalid);
        Assert.assertFalse(this.orderReceiver.customerDoesNotExist);
        Assert.assertTrue(this.orderReceiver.addItemFailed);
    }

    @Test
    public void cannotAddItem_OrderIdIsInvalid() {
        String orderId = UUID.randomUUID().toString();
        ProductInfo productInfo = givenProductInfo("Name", "Description", 20.0, 10);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(productReceiver, productInfo, productRepository);
        registerProduct.execute();

        this.addItem = new AddSalesOrderItemUseCase(orderId, salesRepository, productRepository, orderReceiver);
        addItem.withProductIdAndQuantity(productInfo.id, 5);
        addItem.closeOrder();

        ReadSalesOrderUseCase readOrder = new ReadSalesOrderUseCase(salesRepository);
        SalesOrderInfo info = readOrder.withId(orderId);

        Assert.assertNull(info);
        Assert.assertTrue(this.orderReceiver.addItemFailed);
    }

    @Test
    public void cannotAddTwoItemsWithSameProductId() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");
        CustomerInfo customerInfo = givenCustomer();

        createOrder = new CreateSalesOrderUseCase(orderId, customerInfo.id, salesRepository, customerRepository, orderReceiver, date);
        createOrder.execute();

        this.addItem = new AddSalesOrderItemUseCase(orderId, salesRepository, productRepository, orderReceiver);

        ProductInfo productInfo = givenProductInfo("Name", "Description", 20.0, 10);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(productReceiver, productInfo, productRepository);
        registerProduct.execute();

        addItem.withProductIdAndQuantity(productInfo.id, 5);
        Assert.assertFalse(this.orderReceiver.addItemFailed);
        addItem.withProductIdAndQuantity(productInfo.id, 5);
        Assert.assertTrue(this.orderReceiver.addItemFailed);
        addItem.closeOrder();

        ReadSalesOrderUseCase readOrder = new ReadSalesOrderUseCase(salesRepository);
        SalesOrderInfo info = readOrder.withId(orderId);

        Assert.assertNotNull(info);
        Assert.assertEquals(orderId, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(customerInfo.id, info.customerId);
        Assert.assertEquals(SalesOrderInfo.IN_PROCESS, info.status);
        Assert.assertEquals((20.0 * 5), info.total, 0.01);
        Assert.assertEquals(1, info.items.size());
        Assert.assertFalse(this.orderReceiver.createOrderFailed);
        Assert.assertFalse(this.orderReceiver.salesOrderIdIsInvalid);
        Assert.assertFalse(this.orderReceiver.customerDoesNotExist);
    }

    public void cannotAddItemWithNoSufficientUnitsInStock() {

    }

    public void cannotAddItemToOrderWithStatusDifferentFromOpen() {

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

    private CustomerInfo givenCustomer() {
        CustomerInfo customerInfo = givenCustomerInfo("Name", "99999999999", "99999999999", "Rua AAAA, 999, Bairro BBB, Cidade AAAA, CEP 99999999");
        RegisterCustomerUseCase registerCustomer = new RegisterCustomerUseCase(customerRepository, customerReceiver, customerInfo);
        registerCustomer.execute();
        return customerInfo;
    }

    private CustomerInfo givenCustomerInfo(String name, String cpf, String telephoneNumber, String address) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.id = UUID.randomUUID().toString();
        customerInfo.name = name;
        customerInfo.cpf = cpf;
        customerInfo.telephoneNumber = telephoneNumber;
        customerInfo.address = address;
        customerRepository.saveCustomer(customerInfo);
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
