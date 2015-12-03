package UseCases.Order.Sales;

import Entities.Order.SalesOrderInfo;
import Interfaces.Persistence.SalesOrderRepository;
import TestDoubles.Persistence.InMemorySalesOrderRepository;

/**
 * Created by Bruna Koch Schmitt on 29/11/2015.
 */
public class ReadSalesOrderUseCase {

    private SalesOrderRepository repository;

    public ReadSalesOrderUseCase(SalesOrderRepository salesOrderRepository) {
        this.repository = salesOrderRepository;
    }

    public SalesOrderInfo withId(String id) {
        return this.repository.getById(id);
    }
}
