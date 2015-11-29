package UseCases.Customer;

import Entities.Customer.CustomerInfo;
import Interfaces.Persistence.CustomerRepository;

/**
 * Created by Bruna Koch Schmitt on 28/11/2015.
 */
public class ReadCustomerUseCase {
    private CustomerRepository repository;

    public ReadCustomerUseCase(CustomerRepository repository) {
        this.repository = repository;
    }

    public CustomerInfo withId(String id) {
        return this.repository.getCustomerById(id);
    }
}
