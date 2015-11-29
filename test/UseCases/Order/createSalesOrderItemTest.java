package UseCases.Order;

import Interfaces.Persistence.CustomerRepository;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.SalesOrderReceiver;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Persistence.InMemorySalesOrderRepository;
import TestDoubles.Receiver.FakeSalesOrderReceiver;
import jdk.nashorn.internal.ir.annotations.Ignore;
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
    private SalesOrderReceiver orderReceiver;

    @Before
    public void setUp() throws Exception {
        this.salesRepository = new InMemorySalesOrderRepository();
        this.productRepository = new InMemoryProductRepository();
        this.customerRepository = new InMemoryCustomerRepository();
        this.orderReceiver = new FakeSalesOrderReceiver();
    }

    @Test
    @Ignore
    public void canAddOneItem() {
        String orderId = UUID.randomUUID().toString();
        Date date = givenDate("01/01/2015");
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
