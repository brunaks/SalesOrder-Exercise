package TestDoubles.Persistence;

import Entities.Order.SalesOrderInfo;
import Interfaces.Persistence.SalesOrderRepository;
import Routes.RequestObjects.CreateSalesOrderItemRequest;

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
        if (getById(salesOrderInfo.id) == null) {
            this.salesOrders.add(salesOrderInfo);
        } else {
            SalesOrderInfo info = getById(salesOrderInfo.id);
            this.salesOrders.remove(info);
            this.salesOrders.add(salesOrderInfo);
        }
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
        this.getById(id).status = newStatus;
    }

    @Override
    public void createItem(CreateSalesOrderItemRequest createRequest) {

    }
}
