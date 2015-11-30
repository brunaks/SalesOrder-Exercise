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
    public void canAddOneItem() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");
        CustomerInfo customerInfo = givenCustomer();

        createOrder = new CreateSalesOrderUseCase(orderId, customerInfo.id, salesRepository, customerRepository, orderReceiver, date);
        createOrder.execute();

        this.addItem = new AddSalesOrderItemUseCase(orderId, salesRepository, productRepository);

        ProductInfo productInfo = givenProductInfo("Name", "Description", 20.0, 10);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(productReceiver, productInfo, productRepository);
        registerProduct.execute();

        addItem.withProductIdAndQuantity(productInfo.id, 5);
        addItem.closeOrder();

        ReadSalesOrderUseCase readOrder = new ReadSalesOrderUseCase(salesRepository);
        SalesOrderInfo info = readOrder.withId(orderId);

        Assert.assertNotNull(info);
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
