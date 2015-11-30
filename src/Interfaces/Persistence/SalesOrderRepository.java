package Interfaces.Persistence;

import Entities.Order.OrderItem;
import Entities.Order.SalesOrderInfo;
import Routes.RequestObjects.CreateSalesOrderItemRequest;

import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public interface SalesOrderRepository {
    List<SalesOrderInfo> getAll();
    void save(SalesOrderInfo salesOrderInfo);
    SalesOrderInfo getById(String id);
    void deleteWithId(String id);
    void updateStatus(String id, String newStatus);
    void createItem(CreateSalesOrderItemRequest createRequest);

    void addItem(SalesOrderInfo order, OrderItem item);
}
