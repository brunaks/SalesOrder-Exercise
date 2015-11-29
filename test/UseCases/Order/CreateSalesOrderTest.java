package UseCases.Order;

import Entities.Customer.CustomerInfo;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Receivers.CustomerReceiver;
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
import java.util.List;
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
    }

    @Test
    public void cannotCreateOrder_CustomerDoesNotExist() {
        
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
