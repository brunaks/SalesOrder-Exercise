package Interfaces.Persistence;

import Entities.Customer.CustomerInfo;

/**
 * Created by Bruna Koch Schmitt on 18/11/2015.
 */
public interface CustomerRepository {
    void saveCustomer(CustomerInfo customerInfo);
    CustomerInfo getCustomerByID(String customerID);
}
