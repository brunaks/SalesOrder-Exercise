package UseCases.Order;

import Entities.Order.SalesOrderInfo;
import Interfaces.Persistence.SalesOrderRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class ListSalesOrdersUseCase {

    private SalesOrderRepository salesOrderRepository;

    public ListSalesOrdersUseCase(SalesOrderRepository salesOrderRepository) {
        this.salesOrderRepository = salesOrderRepository;
    }

    public List<SalesOrderInfo> getAll() {
        return salesOrderRepository.getAllSalesOrderInfos();
    }
}
