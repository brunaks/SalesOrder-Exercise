package UseCases.Customer;

import Entities.Customer.CustomerInfo;
import Interfaces.Persistence.CustomerRepository;
import TestDoubles.Persistence.InMemoryCustomerRepository;

import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 28/11/2015.
 */
public class ListCustomersUseCase {
    private CustomerRepository repository;

    public ListCustomersUseCase(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<CustomerInfo> getAll() {
        return this.repository.getAll();
    }
}
