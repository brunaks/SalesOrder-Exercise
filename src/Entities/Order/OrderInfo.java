package Entities.Order;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
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
    public ArrayList<OrderItem> items;
    public double total;
}
