package Entities.Order;

import Entities.Customer.CustomerInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public class SalesOrderInfo {

    public static final String IN_PROCESS = "In Process";
    public String id;
    public Date date;
    public String status;
    public CustomerInfo customerInfo;
    public List<SalesOrderItem> items;
    public double total;
}
