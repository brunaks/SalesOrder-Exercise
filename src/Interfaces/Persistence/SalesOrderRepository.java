package Interfaces.Persistence;

import Entities.Order.SalesOrderInfo;

import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public interface SalesOrderRepository {
    List<SalesOrderInfo> getAllSalesOrderInfos();
    void save(SalesOrderInfo salesOrderInfo);
}
