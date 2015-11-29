package Interfaces.Persistence;

import Entities.Customer.CustomerInfo;

import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 18/11/2015.
 */
public interface CustomerRepository {
    void saveCustomer(CustomerInfo customerInfo);

    CustomerInfo getCustomerById(String customerID);

    List<CustomerInfo> getAll();

    String generateId();
}
