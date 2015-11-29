package UseCases.Customer;

import Entities.Customer.CustomerInfo;
import Interfaces.Persistence.CustomerRepository;
import Interfaces.Receivers.CustomerReceiver;

/**
 * Created by Bruna Koch Schmitt on 28/11/2015.
 */
public class RegisterCustomerUseCase {

    private CustomerInfo info;
    private CustomerReceiver receiver;
    private CustomerRepository repository;

    public RegisterCustomerUseCase(CustomerRepository repository, CustomerReceiver receiver, CustomerInfo info) {
        this.repository = repository;
        this.receiver = receiver;
        this.info = info;
    }

    public void execute() {
        if (this.info.isValid()) {
            this.repository.saveCustomer(this.info);
        } else {
            this.receiver.registerFailed();
            this.receiver.customerInfoIsInvalid();
        }
    }


}
