package Entities.Order;

import java.util.Date;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 21/11/2015.
 */
public abstract class OrderInfo {

    public static final String IN_PROCESS = "In Process";
    public static final String DELIVERED = "Delivered";
    public String id;
    public Date date;
    public String status;
    public List<OrderItem> items;
    public double total;
}
