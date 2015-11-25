package UseCases.Order;

import Entities.Customer.CustomerInfo;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Persistence.InMemorySalesOrderRepository;
import TestDoubles.Receiver.FakeProductReceiver;
import TestDoubles.Receiver.FakeSalesOrderReceiver;
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
public class UpdateSalesOrderStatusTest {

    private InMemorySalesOrderRepository salesOrderRepository;
    private InMemoryProductRepository productRepository;
    private InMemoryCustomerRepository customerRepository;
    private FakeSalesOrderReceiver receiver;
    CreateSalesOrderUseCase createOrder;
    ListSalesOrdersUseCase listSalesOrdersUseCase;

    @Before
    public void setUp() throws Exception {
        salesOrderRepository = new InMemorySalesOrderRepository();
        productRepository = new InMemoryProductRepository();
        customerRepository = new InMemoryCustomerRepository();
        receiver = new FakeSalesOrderReceiver();
    }

    @Test
    public void canChangeStatusFromInProcessToDelivered() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createOrder = new CreateSalesOrderUseCase(id, salesOrderRepository, productRepository, customerRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name", "Description", 10, 10);
        createOrder.addProduct(productInfo.id, 1);
        CustomerInfo customerInfo = givenCustomerInfo("Name", "99999999999", "99999999999", "Rua AAAA, 999, Bairro BBB, Cidade AAAA, CEP 99999999");
        createOrder.addCustomer(customerInfo.id);
        createOrder.execute();
        Assert.assertFalse(receiver.orderFailed);

        UpdateSalesOrderStatusUseCase updateOrderStatus = new UpdateSalesOrderStatusUseCase(id, salesOrderRepository, receiver);
        updateOrderStatus.changeTo(SalesOrderInfo.DELIVERED);

        listSalesOrdersUseCase = new ListSalesOrdersUseCase(salesOrderRepository);
        List<SalesOrderInfo> orders = listSalesOrdersUseCase.getAll();
        Assert.assertEquals(SalesOrderInfo.DELIVERED, orders.get(0).status);
    }

    @Test
    public void cannotChangeStatusFromInProcessToDelivered_OrderIdIsInvalid() {
        String id = UUID.randomUUID().toString();
        UpdateSalesOrderStatusUseCase updateOrderStatus = new UpdateSalesOrderStatusUseCase(id, salesOrderRepository, receiver);
        updateOrderStatus.changeTo(SalesOrderInfo.DELIVERED);
        Assert.assertTrue(receiver.statusUpdateFailed);
        Assert.assertTrue(receiver.salesOrderIdIsInvalid);
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
