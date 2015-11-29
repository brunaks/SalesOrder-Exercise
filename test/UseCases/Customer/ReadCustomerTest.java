package UseCases.Customer;

import Entities.Customer.Customer;
import Entities.Customer.CustomerInfo;
import Interfaces.Receivers.CustomerReceiver;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import TestDoubles.Receiver.FakeCustomerReceiver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 28/11/2015.
 */
public class ReadCustomerTest {

    private InMemoryCustomerRepository repository;
    private FakeCustomerReceiver receiver;

    @Before
    public void setUp() throws Exception {
        this.repository = new InMemoryCustomerRepository();
        this.receiver = new FakeCustomerReceiver();
    }

    @Test
    public void canReadOneCustomer() {
        CustomerInfo info = givenCustomerInfo("Name", "99999999999", "9999999999", "Address");
        RegisterCustomerUseCase register = new RegisterCustomerUseCase(this.repository, this.receiver, info);
        register.execute();
        Assert.assertFalse(this.receiver.infoIsInvalid);
        Assert.assertFalse(this.receiver.registerFailed);

        ReadCustomerUseCase read = new ReadCustomerUseCase(this.repository);
        CustomerInfo infoRetrieved = read.withId(info.id);
        Assert.assertEquals(info.id, infoRetrieved.id);
        Assert.assertEquals(info.name, infoRetrieved.name);
        Assert.assertEquals(info.cpf, infoRetrieved.cpf);
        Assert.assertEquals(info.telephoneNumber, infoRetrieved.telephoneNumber);
        Assert.assertEquals(info.address, infoRetrieved.address);
    }

    @Test
    public void canReadTwoCustomers() {
        CustomerInfo info = givenCustomerInfo("Name", "99999999999", "9999999999", "Address");
        RegisterCustomerUseCase register = new RegisterCustomerUseCase(this.repository, this.receiver, info);
        register.execute();
        Assert.assertFalse(this.receiver.infoIsInvalid);
        Assert.assertFalse(this.receiver.registerFailed);

        CustomerInfo info2 = givenCustomerInfo("Name", "99999999999", "9999999999", "Address");
        register = new RegisterCustomerUseCase(this.repository, this.receiver, info2);
        register.execute();
        Assert.assertFalse(this.receiver.infoIsInvalid);
        Assert.assertFalse(this.receiver.registerFailed);

        ReadCustomerUseCase read = new ReadCustomerUseCase(this.repository);
        CustomerInfo infoRetrieved = read.withId(info.id);
        Assert.assertEquals(info.id, infoRetrieved.id);
        Assert.assertEquals(info.name, infoRetrieved.name);
        Assert.assertEquals(info.cpf, infoRetrieved.cpf);
        Assert.assertEquals(info.telephoneNumber, infoRetrieved.telephoneNumber);
        Assert.assertEquals(info.address, infoRetrieved.address);

        read = new ReadCustomerUseCase(this.repository);
        CustomerInfo infoRetrieved2 = read.withId(info2.id);
        Assert.assertEquals(info2.id, infoRetrieved2.id);
        Assert.assertEquals(info2.name, infoRetrieved2.name);
        Assert.assertEquals(info2.cpf, infoRetrieved2.cpf);
        Assert.assertEquals(info2.telephoneNumber, infoRetrieved2.telephoneNumber);
        Assert.assertEquals(info2.address, infoRetrieved2.address);
    }

    @Test
    public void cannotReadCustomer_CustomerDOesNotExist() {
        ReadCustomerUseCase read = new ReadCustomerUseCase(this.repository);
        CustomerInfo infoRetrieved = read.withId(UUID.randomUUID().toString());
        Assert.assertNull(infoRetrieved);
    }

    private CustomerInfo givenCustomerInfo(String name, String cpf, String telephoneNumber, String address) {
        CustomerInfo info = new CustomerInfo();
        info.id = UUID.randomUUID().toString();
        info.name = name;
        info.cpf = cpf;
        info.telephoneNumber = telephoneNumber;
        info.address = address;
        return info;
    }
}
