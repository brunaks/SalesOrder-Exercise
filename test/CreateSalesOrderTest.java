import Entities.Customer.CustomerInfo;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.OrderReceiver;
import UseCases.Order.CreateSalesOrder;
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
    SalesOrderRepository repository;
    CreateSalesOrder createOrder;
    private SalesOrderInfo orderInfo;
    InMemoryProductRepository productRepository;

    @Before
    public void setUp() throws Exception {
        receiver = new FakeSalesOrderReceiver();
        repository = new InMemoryOrderRepository();
        productRepository = new InMemoryProductRepository();
    }

    @Test
    public void canCreateOrderWithSuccess_OneProduct() {
        Date date = givenDate("01/01/2015");
        createOrder = new CreateSalesOrder(repository, productRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name", "Description", 10, 10);
        createOrder.addProduct(productInfo.id, 1);
        createOrder.addCustomer(new CustomerInfo());
        Assert.assertFalse(receiver.orderFailed);
        Assert.assertEquals(10, createOrder.getTotal(), 0.01);
    }

    @Test
    public void canCreateOrderWithSuccess_TwoProducts() {
        Date date = givenDate("01/01/2015");
        createOrder = new CreateSalesOrder(repository, productRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name1", "Description1", 10, 10);
        ProductInfo productInfo2 = givenProductInfo("Name2", "Description2", 20, 20);
        createOrder.addProduct(productInfo.id,  1);
        createOrder.addProduct(productInfo2.id, 2);
        createOrder.addCustomer(new CustomerInfo());
        Assert.assertFalse(receiver.orderFailed);
        Assert.assertEquals(50, createOrder.getTotal(), 0.01);
    }

    @Test
    public void createOrderFailed_productIdDoesNotExist() {
        Date date = givenDate("01/01/2015");
        createOrder = new CreateSalesOrder(repository, productRepository, receiver, date);
        createOrder.addProduct(UUID.randomUUID().toString(), 1);
        createOrder.addCustomer(new CustomerInfo());
        Assert.assertTrue(receiver.orderFailed);
        Assert.assertEquals(0, createOrder.getTotal(), 0.01);
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
        productInfo.id = UUID.randomUUID().toString();
        productInfo.id = productRepository.createProductInfoID();
        productRepository.saveProduct(productInfo);
        return productInfo;
    }

    private SalesOrderInfo createOrderInfo() {
        SalesOrderInfo info = new SalesOrderInfo();
        return info;
    }
}
