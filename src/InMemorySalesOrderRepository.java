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
    public List<SalesOrderInfo> getAllSalesOrderInfos() {
        return this.salesOrders;
    }

    @Override
    public void save(SalesOrderInfo salesOrderInfo) {
        this.addInProcessStatusToSalesOrder(salesOrderInfo);
        this.salesOrders.add(salesOrderInfo);
    }

    private void addInProcessStatusToSalesOrder(SalesOrderInfo salesOrderInfo) {
        salesOrderInfo.status = SalesOrderInfo.IN_PROCESS;
    }
}
