package UseCases.Order;

import Entities.Customer.CustomerInfo;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Persistence.InMemorySalesOrderRepository;
import TestDoubles.Receiver.FakeProductReceiver;
import TestDoubles.Receiver.FakeSalesOrderReceiver;
import UseCases.Order.CreateSalesOrderUseCase;
import UseCases.Order.ListSalesOrdersUseCase;
import UseCases.Product.RegisterProductUseCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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

    @Before
    public void setUp() throws Exception {
        receiver = new FakeSalesOrderReceiver();
        salesOrderRepository = new InMemorySalesOrderRepository();
        productRepository = new InMemoryProductRepository();
        customerRepository = new InMemoryCustomerRepository();
    }

    @Test
    public void canCreateOrderWithSuccess_OneProduct() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createOrder = new CreateSalesOrderUseCase(id, salesOrderRepository, productRepository, customerRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name", "Description", 10, 10);
        createOrder.addProduct(productInfo.id, 1);
        CustomerInfo customerInfo = givenCustomerInfo("Name", "99999999999", "99999999999", "Rua AAAA, 999, Bairro BBB, Cidade AAAA, CEP 99999999");
        createOrder.addCustomer(customerInfo.id);
        createOrder.execute();
        Assert.assertFalse(receiver.orderFailed);
        assertTotalEquals(10.0);
    }


    @Test
    public void canCreateOrderWithSuccess_TwoProducts() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createOrder = new CreateSalesOrderUseCase(id, salesOrderRepository, productRepository, customerRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name1", "Description1", 10, 10);
        ProductInfo productInfo2 = givenProductInfo("Name2", "Description2", 20, 20);
        createOrder.addProduct(productInfo.id, 1);
        createOrder.addProduct(productInfo2.id, 2);
        CustomerInfo customerInfo = givenCustomerInfo("Name", "99999999999", "99999999999", "Rua AAAA, 999, Bairro BBB, Cidade AAAA, CEP 99999999");
        createOrder.addCustomer(customerInfo.id);
        Assert.assertFalse(receiver.orderFailed);
        assertTotalEquals(50.0);
    }

    @Test
    public void createOrderFailed_productIdDoesNotExist() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createOrder = new CreateSalesOrderUseCase(id, salesOrderRepository, productRepository, customerRepository, receiver, date);
        createOrder.addProduct(UUID.randomUUID().toString(), 1);
        CustomerInfo customerInfo = givenCustomerInfo("Name", "99999999999", "99999999999", "Rua AAAA, 999, Bairro BBB, Cidade AAAA, CEP 99999999");
        createOrder.addCustomer(customerInfo.id);
        Assert.assertTrue(receiver.orderFailed);
        Assert.assertTrue(receiver.productDoesNotExist);
        Assert.assertFalse(receiver.customerDoesNotExist);
        assertTotalEquals(0.0);
    }


    @Test
    public void createOrderFailed_clientIdDoesNotExist() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createOrder = new CreateSalesOrderUseCase(id, salesOrderRepository, productRepository, customerRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name1", "Description1", 10, 10);
        createOrder.addProduct(productInfo.id, 1);
        createOrder.addCustomer(UUID.randomUUID().toString());
        Assert.assertTrue(receiver.orderFailed);
        Assert.assertTrue(receiver.customerDoesNotExist);
        Assert.assertFalse(receiver.productDoesNotExist);
        assertTotalEquals(0.0);
    }

    @Test
    public void createOrderSuccessful_canRetrieveSalesOrder() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createOrder = new CreateSalesOrderUseCase(id, salesOrderRepository, productRepository, customerRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name1", "Description1", 10, 10);
        createOrder.addProduct(productInfo.id, 1);
        CustomerInfo customerInfo = givenCustomerInfo("Name", "99999999999", "99999999999", "Rua AAAA, 999, Bairro BBB, Cidade AAAA, CEP 99999999");
        createOrder.addCustomer(customerInfo.id);
        createOrder.execute();
        Assert.assertFalse(receiver.orderFailed);
        Assert.assertFalse(receiver.productDoesNotExist);
        Assert.assertFalse(receiver.customerDoesNotExist);
        assertTotalEquals(10.0);
        ListSalesOrdersUseCase listSalesOrders = new ListSalesOrdersUseCase(salesOrderRepository);
        List<SalesOrderInfo> salesOrderInfos = listSalesOrders.getAll();

        Assert.assertEquals(id, salesOrderInfos.get(0).id);
        Assert.assertEquals(date, salesOrderInfos.get(0).date);
        Assert.assertEquals(SalesOrderInfo.IN_PROCESS, salesOrderInfos.get(0).status);
    }

    private void assertTotalEquals(Double expectedTotal) {
        Assert.assertEquals(expectedTotal, createOrder.getTotal(), 0.01);
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
