package UseCases.Order;

import Entities.Customer.CustomerInfo;
import Entities.Order.SalesOrderInfo;
import Interfaces.Receivers.CustomerReceiver;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Persistence.InMemorySalesOrderRepository;
import TestDoubles.Receiver.FakeCustomerReceiver;
import TestDoubles.Receiver.FakeSalesOrderReceiver;
import UseCases.Customer.RegisterCustomerUseCase;
import UseCases.Order.Sales.CreateSalesOrderUseCase;
import UseCases.Order.Sales.ReadSalesOrderUseCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public class CreateSalesOrderTest {

    FakeSalesOrderReceiver receiver;
    InMemorySalesOrderRepository salesOrderRepository;
    CreateSalesOrderUseCase createOrder;
    InMemoryProductRepository productRepository;
    InMemoryCustomerRepository customerRepository;
    private CustomerReceiver customerReceiver;

    @Before
    public void setUp() throws Exception {
        receiver = new FakeSalesOrderReceiver();
        salesOrderRepository = new InMemorySalesOrderRepository();
        productRepository = new InMemoryProductRepository();
        customerRepository = new InMemoryCustomerRepository();
        customerReceiver = new FakeCustomerReceiver();
    }

    @Test
    public void canCreateOrderWithSuccess() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        CustomerInfo customerInfo = givenCustomer();

        createOrder = new CreateSalesOrderUseCase(id, customerInfo.id, salesOrderRepository, customerRepository, receiver, date);
        createOrder.execute();

        ReadSalesOrderUseCase readOrder = new ReadSalesOrderUseCase(salesOrderRepository);
        SalesOrderInfo info = readOrder.withId(id);

        Assert.assertNotNull(info);
        Assert.assertEquals(id, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(customerInfo.id, info.customerId);
        Assert.assertEquals(SalesOrderInfo.OPEN, info.status);
        Assert.assertEquals(0.0, info.total, 0.01);
        Assert.assertEquals(0, info.items.size());
        Assert.assertFalse(this.receiver.createOrderFailed);
        Assert.assertFalse(this.receiver.salesOrderIdIsInvalid);
        Assert.assertFalse(this.receiver.customerDoesNotExist);
    }

    @Test
    public void cannotCreateOrder_CustomerDoesNotExist() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        String customerId = UUID.randomUUID().toString();

        createOrder = new CreateSalesOrderUseCase(id, customerId, salesOrderRepository, customerRepository, receiver, date);
        createOrder.execute();

        ReadSalesOrderUseCase readOrder = new ReadSalesOrderUseCase(salesOrderRepository);
        SalesOrderInfo info = readOrder.withId(id);

        Assert.assertNull(info);
        Assert.assertTrue(this.receiver.createOrderFailed);
        Assert.assertFalse(this.receiver.salesOrderIdIsInvalid);
        Assert.assertTrue(this.receiver.customerDoesNotExist);
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
