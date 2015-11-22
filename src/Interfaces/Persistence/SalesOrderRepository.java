package Interfaces.Persistence;

import Entities.Order.SalesOrderInfo;

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
}
