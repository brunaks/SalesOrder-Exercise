package UseCases.Customer;

import Entities.Customer.CustomerInfo;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import TestDoubles.Receiver.FakeCustomerReceiver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 28/11/2015.
 */
public class RegisterCustomerTest {

    private InMemoryCustomerRepository repository;
    private FakeCustomerReceiver receiver;
    private RegisterCustomerUseCase register;

    @Before
    public void setUp() throws Exception {
        this.repository = new InMemoryCustomerRepository();
        this.receiver = new FakeCustomerReceiver();
    }

    @Test
    public void canRegisterCustomerSuccessfully() {
        CustomerInfo info = givenCustomerInfo("Name", "99999999999", "9999999999", "Address");
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertFalse(receiver.registerFailed);
        Assert.assertFalse(receiver.infoIsInvalid);
    }

    @Test
    public void cannotRegisterCustomer_idCannotBeBlank() {
        CustomerInfo info = givenCustomerInfo("Name", "99999999999", "9999999999", "Address");
        info.id = " ";
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertTrue(receiver.registerFailed);
        Assert.assertTrue(receiver.infoIsInvalid);
    }

    @Test
    public void cannotRegisterCustomer_nameCannotBeBlank() {
        CustomerInfo info = givenCustomerInfo(" ", "99999999999", "9999999999", "Address");
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertTrue(receiver.registerFailed);
        Assert.assertTrue(receiver.infoIsInvalid);
    }

    @Test
    public void cannotRegisterCustomer_cpfCannotBeBlank() {
        CustomerInfo info = givenCustomerInfo("Name", " ", "9999999999", "Address");
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertTrue(receiver.registerFailed);
        Assert.assertTrue(receiver.infoIsInvalid);
    }

    @Test
    public void cannotRegisterCustomer_cpfMustHaveElevenCharacters() {
        CustomerInfo info = givenCustomerInfo("Name", "9999999999", "9999999999", "Address");
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertTrue(receiver.registerFailed);
        Assert.assertTrue(receiver.infoIsInvalid);

        info.cpf = "999999999999";
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertTrue(receiver.registerFailed);
        Assert.assertTrue(receiver.infoIsInvalid);
    }

    @Test
    public void cannotRegisterCustomer_telephoneNumberCannotBeBlank() {
        CustomerInfo info = givenCustomerInfo("Name", "99999999999", " ", "Address");
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertTrue(receiver.registerFailed);
        Assert.assertTrue(receiver.infoIsInvalid);
    }

    @Test
    public void cannotRegisterCustomer_telephoneNumberMustHaveTenCharacters_TwoForDDDAndEightForNumber() {
        CustomerInfo info = givenCustomerInfo("Name", "99999999999", "999999999", "Address");
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertTrue(receiver.registerFailed);
        Assert.assertTrue(receiver.infoIsInvalid);

        info.telephoneNumber = "99999999999";
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertTrue(receiver.registerFailed);
        Assert.assertTrue(receiver.infoIsInvalid);
    }

    @Test
    public void cannotRegisterCustomer_addressCannotBeBlank() {
        CustomerInfo info = givenCustomerInfo("Name", "99999999999", "9999999999", " ");
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertTrue(receiver.registerFailed);
        Assert.assertTrue(receiver.infoIsInvalid);
    }

    @Test
    public void canRegisterAndReadCustomerInformationById() {
        CustomerInfo info = givenCustomerInfo("Name", "99999999999", "9999999999", "Address");
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertFalse(receiver.registerFailed);

        ReadCustomerUseCase readCustomer = new ReadCustomerUseCase(this.repository);
        CustomerInfo infoRetrieved = readCustomer.withId(info.id);
        Assert.assertEquals(info.id, infoRetrieved.id);
        Assert.assertEquals(info.name, infoRetrieved.name);
        Assert.assertEquals(info.cpf, infoRetrieved.cpf);
        Assert.assertEquals(info.telephoneNumber, infoRetrieved.telephoneNumber);
        Assert.assertEquals(info.address, infoRetrieved.address);
    }

    @Test
    public void cannotRegisterAndReadCustomerInformationById_infoIsInvalid_customerIsNotSaved() {
        CustomerInfo info = givenCustomerInfo("Name", "99999999999", "9999999999", "Address");
        info.name = " ";
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertTrue(receiver.registerFailed);

        ReadCustomerUseCase readCustomer = new ReadCustomerUseCase(this.repository);
        CustomerInfo infoRetrieved = readCustomer.withId(info.id);
        Assert.assertNull(infoRetrieved);
    }

    @Test
    public void canRegisterTwoCustomersAndReadTheirInformationByTheirIds() {
        CustomerInfo info = givenCustomerInfo("Name", "99999999999", "9999999999", "Address");
        this.register = new RegisterCustomerUseCase(repository, receiver, info);
        register.execute();
        Assert.assertFalse(receiver.registerFailed);

        CustomerInfo info2 = givenCustomerInfo("Name2", "88888888888", "8888888888", "Address2");
        this.register = new RegisterCustomerUseCase(repository, receiver, info2);
        register.execute();
        Assert.assertFalse(receiver.registerFailed);

        ReadCustomerUseCase readCustomer = new ReadCustomerUseCase(this.repository);
        CustomerInfo infoRetrieved = readCustomer.withId(info.id);
        Assert.assertEquals(info.id, infoRetrieved.id);
        Assert.assertEquals(info.name, infoRetrieved.name);
        Assert.assertEquals(info.cpf, infoRetrieved.cpf);
        Assert.assertEquals(info.telephoneNumber, infoRetrieved.telephoneNumber);
        Assert.assertEquals(info.address, infoRetrieved.address);

        readCustomer = new ReadCustomerUseCase(this.repository);
        CustomerInfo infoRetrieved2 = readCustomer.withId(info2.id);
        Assert.assertEquals(info2.id, infoRetrieved2.id);
        Assert.assertEquals(info2.name, infoRetrieved2.name);
        Assert.assertEquals(info2.cpf, infoRetrieved2.cpf);
        Assert.assertEquals(info2.telephoneNumber, infoRetrieved2.telephoneNumber);
        Assert.assertEquals(info2.address, infoRetrieved2.address);
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
