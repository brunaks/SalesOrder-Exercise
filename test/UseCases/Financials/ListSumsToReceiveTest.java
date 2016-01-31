package UseCases.Financials;

import Entities.Customer.CustomerInfo;
import Entities.FinancialRecords.SumToReceiveInfo;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import Persistence.InMemorySumsToReceiveRepository;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Persistence.InMemorySalesOrderRepository;
import TestDoubles.Receiver.FakeCustomerReceiver;
import TestDoubles.Receiver.FakeProductReceiver;
import TestDoubles.Receiver.FakeSalesOrderReceiver;
import UseCases.Customer.RegisterCustomerUseCase;
import UseCases.Order.Sales.AddSalesOrderItemUseCase;
import UseCases.Order.Sales.CreateSalesOrderUseCase;
import UseCases.Product.RegisterProductUseCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 31/01/2016.
 */
public class ListSumsToReceiveTest {

    ListSumsToReceiveUseCase listSums;
    InMemorySumsToReceiveRepository repository;


    @Before
    public void setup() throws Exception {

    }

    @Test
    public void salesOrderIsOpen_noSumToReceiveExists() {
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        ProductInfo productInfo = givenRegisteredProduct(productRepository);

        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        CustomerInfo customerInfo = givenRegisteredCustomer(customerRepository);

        InMemorySalesOrderRepository salesOrderRepository = new InMemorySalesOrderRepository();
        FakeSalesOrderReceiver salesOrderReceiver = new FakeSalesOrderReceiver();
        String salesOrderId = givenRegisteredSalesOrder(customerRepository, customerInfo, salesOrderRepository, salesOrderReceiver);

        AddSalesOrderItemUseCase addItem = new AddSalesOrderItemUseCase(salesOrderId, salesOrderRepository, productRepository, salesOrderReceiver, repository);
        addItem.withProductIdAndQuantity(productInfo.id, 5);

        Assert.assertEquals(SalesOrderInfo.OPEN, salesOrderRepository.getById(salesOrderId).status);
        repository = new InMemorySumsToReceiveRepository();
        listSums = new ListSumsToReceiveUseCase(repository);
        Assert.assertEquals(0, listSums.getAll().size());
    }

    @Test
    public void salesOrderIsSetToInProcess_sumToReceiveIsCreated() {
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        ProductInfo productInfo = givenRegisteredProduct(productRepository);

        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        CustomerInfo customerInfo = givenRegisteredCustomer(customerRepository);

        InMemorySalesOrderRepository salesOrderRepository = new InMemorySalesOrderRepository();
        FakeSalesOrderReceiver salesOrderReceiver = new FakeSalesOrderReceiver();
        String salesOrderId = givenRegisteredSalesOrder(customerRepository, customerInfo, salesOrderRepository, salesOrderReceiver);

        repository = new InMemorySumsToReceiveRepository();
        AddSalesOrderItemUseCase addItem = new AddSalesOrderItemUseCase(salesOrderId, salesOrderRepository, productRepository, salesOrderReceiver, repository);
        addItem.withProductIdAndQuantity(productInfo.id, 5);
        Assert.assertEquals(SalesOrderInfo.OPEN, salesOrderRepository.getById(salesOrderId).status);
        addItem.setOrderToProcessing();
        Assert.assertEquals(SalesOrderInfo.IN_PROCESS, salesOrderRepository.getById(salesOrderId).status);

        listSums = new ListSumsToReceiveUseCase(repository);
        Assert.assertEquals(1, listSums.getAll().size());
        Assert.assertEquals(salesOrderId, listSums.getAll().get(0).orderId);
        Assert.assertEquals(SumToReceiveInfo.OPEN, listSums.getAll().get(0).payStatus);
    }

    private String givenRegisteredSalesOrder(InMemoryCustomerRepository customerRepository, CustomerInfo customerInfo, InMemorySalesOrderRepository salesOrderRepository, FakeSalesOrderReceiver salesOrderReceiver) {
        String salesOrderId = UUID.randomUUID().toString();
        Date salesOrderDate = givenDate("31/01/2016");
        CreateSalesOrderUseCase createSalesOrder = new CreateSalesOrderUseCase(salesOrderId, customerInfo.id, salesOrderRepository, customerRepository, salesOrderReceiver, salesOrderDate);
        createSalesOrder.execute();
        return salesOrderId;
    }

    private CustomerInfo givenRegisteredCustomer(InMemoryCustomerRepository customerRepository) {
        CustomerInfo customerInfo = giverCustomer(customerRepository);
        RegisterCustomerUseCase registerCustomer = new RegisterCustomerUseCase(customerRepository, new FakeCustomerReceiver(), customerInfo);
        registerCustomer.execute();
        return customerInfo;
    }

    private ProductInfo givenRegisteredProduct(InMemoryProductRepository productRepository) {
        ProductInfo productInfo = givenProduct(productRepository);
        RegisterProductUseCase registerProduct = new RegisterProductUseCase(new FakeProductReceiver(), productInfo, productRepository);
        registerProduct.execute();
        return productInfo;
    }

    private CustomerInfo giverCustomer(InMemoryCustomerRepository customerRepository) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.id = customerRepository.generateId();
        customerInfo.name = "Customer Name";
        customerInfo.address = "Customer Address";
        customerInfo.cpf = "99999999999";
        customerInfo.telephoneNumber = "9999999999";
        return customerInfo;
    }

    private ProductInfo givenProduct(InMemoryProductRepository productRepository) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.id = productRepository.createProductInfoID();
        productInfo.name = "Product Name";
        productInfo.unitsInStock = 10;
        productInfo.description = "Product Description";
        productInfo.price = 10.0;
        return productInfo;
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
