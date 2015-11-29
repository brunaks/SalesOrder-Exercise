package UseCases.Customer;

import Entities.Customer.CustomerInfo;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 28/11/2015.
 */
public class ListCustomersTest {

    private InMemoryCustomerRepository repository;

    @Before
    public void setUp() throws Exception {
        this.repository = new InMemoryCustomerRepository();
    }

    @Test
    public void noCustomersRegistered_listIsEmpty() {
        ListCustomersUseCase list = new ListCustomersUseCase(this.repository);
        List<CustomerInfo> customers = list.getAll();
        Assert.assertEquals(0, customers.size());
    }
}
