package UseCases.Order;

import Entities.Order.SalesOrderInfo;
import Interfaces.Persistence.SalesOrderRepository;
import Routes.RequestObjects.SalesOrderSummary;

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
        return salesOrderRepository.getAll();
    }

    public List<SalesOrderSummary> getAllSummaries() {
        return buildSummary(salesOrderRepository.getAll());
    }

    private List<SalesOrderSummary> buildSummary(List<SalesOrderInfo> salesOrderInfos) {
        List<SalesOrderSummary> summaries = new ArrayList<>();
        for (SalesOrderInfo info : salesOrderInfos) {
            SalesOrderSummary summary = new SalesOrderSummary();
            summary.id = info.id;
            summary.date = info.date;
            summary.status = info.status;
            summary.total = info.total;
            summary.customerId = info.customerId;
            summaries.add(summary);
        }
        return summaries;
    }
}
