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
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class DeleteSalesOrderTest {

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
    public void canDeleteSalesOrderSuccessfully() {
        SalesOrderInfo salesOrderInfo = givenValidSalesOrder();
        DeleteSalesOrderUseCase deleteSalesOrder = new DeleteSalesOrderUseCase(salesOrderRepository, receiver);
        deleteSalesOrder.executeWithSalesOrderID(salesOrderInfo.id);
        this.assertSalesOrderWasDeleted();
    }

    @Test
    public void cannotDeleteSalesOrder_IdIsInvalid_OrderDoesNotExist() {
        DeleteSalesOrderUseCase deleteSalesOrder = new DeleteSalesOrderUseCase(salesOrderRepository, receiver);
        deleteSalesOrder.executeWithSalesOrderID(UUID.randomUUID().toString());
        Assert.assertTrue(receiver.salesOrderIdIsInvalid);
        Assert.assertTrue(receiver.deleteFailed);
    }

    private void assertSalesOrderWasDeleted() {
        ListSalesOrdersUseCase listSalesOrdersUseCase = new ListSalesOrdersUseCase(salesOrderRepository);
        List<SalesOrderInfo> salesOrderInfos = listSalesOrdersUseCase.getAll();
        Assert.assertEquals(0, salesOrderInfos.size());

        Assert.assertFalse(this.receiver.deleteFailed);
        Assert.assertFalse(this.receiver.salesOrderIdIsInvalid);
    }

    private SalesOrderInfo givenValidSalesOrder() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        CustomerInfo customerInfo = givenCustomerInfo("Name", "99999999999", "99999999999", "Rua AAAA, 999, Bairro BBB, Cidade AAAA, CEP 99999999");
        customerRepository.saveCustomer(customerInfo);
        createOrder = new CreateSalesOrderUseCase(id, customerInfo.id, salesOrderRepository, customerRepository, receiver, date);
        createOrder.execute();
        Assert.assertFalse(receiver.createOrderFailed);
        ListSalesOrdersUseCase listSalesOrdersUseCase = new ListSalesOrdersUseCase(salesOrderRepository);
        List<SalesOrderInfo> salesOrderInfos = listSalesOrdersUseCase.getAll();
        return salesOrderInfos.get(0);
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
