package TestDoubles.Persistence;

import Entities.Order.SalesOrderInfo;
import Interfaces.Persistence.SalesOrderRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class InMemorySalesOrderRepository implements SalesOrderRepository {

    private List<SalesOrderInfo> salesOrders = new ArrayList<SalesOrderInfo>();

    @Override
    public List<SalesOrderInfo> getAll() {
        return this.salesOrders;
    }

    @Override
    public void save(SalesOrderInfo salesOrderInfo) {
        this.salesOrders.add(salesOrderInfo);
    }

    @Override
    public SalesOrderInfo getById(String id) {
        for (SalesOrderInfo salesOrderInfo : salesOrders) {
            if (salesOrderInfo.id == id) {
                return salesOrderInfo;
            }
        }
        return null;
    }

    @Override
    public void deleteWithId(String id) {
        salesOrders.remove(this.getById(id));
    }

    @Override
    public void updateStatus(String id, String newStatus) {
        SalesOrderInfo salesOrderInfo = this.getById(id);
        salesOrderInfo.status = newStatus;
    }
}
