package UseCases.Order;

import Entities.Customer.CustomerInfo;
import Entities.Product.ProductInfo;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Persistence.InMemorySalesOrderRepository;
import TestDoubles.Receiver.FakeProductReceiver;
import TestDoubles.Receiver.FakeSalesOrderReceiver;
import UseCases.Order.Sales.CreateSalesOrderUseCase;
import UseCases.Order.Sales.ListSalesOrdersUseCase;
import UseCases.Product.RegisterProductUseCase;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 22/11/2015.
 */
public class UpdateSalesOrderStatusTest {

    CreateSalesOrderUseCase createOrder;
    ListSalesOrdersUseCase listSalesOrdersUseCase;
    private InMemorySalesOrderRepository salesOrderRepository;
    private InMemoryProductRepository productRepository;
    private InMemoryCustomerRepository customerRepository;
    private FakeSalesOrderReceiver receiver;

    @Before
    public void setUp() throws Exception {
        salesOrderRepository = new InMemorySalesOrderRepository();
        productRepository = new InMemoryProductRepository();
        customerRepository = new InMemoryCustomerRepository();
        receiver = new FakeSalesOrderReceiver();
    }

    @Test
    public void canChangeStatusFromInProcessToDelivered() {

    }

    @Test
    public void cannotChangeStatusFromInProcessToDelivered_OrderIdIsInvalid() {

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
