package TestDoubles.Persistence;

import Entities.Customer.CustomerInfo;

import java.util.ArrayList;
import java.util.List;

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
    public CustomerInfo getCustomerByID(String customerID) {
        for (CustomerInfo customer : this.customers) {
            if (customer.id == customerID) {
                return customer;
            }
        }
        return null;
    }
}
