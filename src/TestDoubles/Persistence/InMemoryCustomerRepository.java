package TestDoubles.Persistence;

import Entities.Customer.CustomerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 18/11/2015.
 */
public class InMemoryCustomerRepository implements Interfaces.Persistence.CustomerRepository {

    private List<CustomerInfo> customers = new ArrayList<CustomerInfo>();

    @Override
    public void saveCustomer(CustomerInfo customerInfo) {
        this.customers.add(customerInfo);
    }

    @Override
    public CustomerInfo getCustomerById(String customerID) {
        for (CustomerInfo customer : this.customers) {
            if (customer.id.equals(customerID)) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public List<CustomerInfo> getAll() {
        return customers;
    }

    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
