package UseCases.Order;

import Entities.Order.PurchaseOrderInfo;
import Interfaces.Persistence.PurchaseOrderRepository;
import Routes.RequestObjects.PurchaseOrderSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 21/11/2015.
 */
public class ListPurchaseOrdersUseCase {

    private PurchaseOrderRepository purchaseOrderRepository;

    public ListPurchaseOrdersUseCase(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public List<PurchaseOrderInfo> getAll() {
        return this.purchaseOrderRepository.getAll();

    }

    public List<PurchaseOrderSummary> getAllSummaries() {
        return buildSummary(purchaseOrderRepository.getAll());
    }

    private List<PurchaseOrderSummary> buildSummary(List<PurchaseOrderInfo> purchaseOrderInfos) {
        List<PurchaseOrderSummary> summaries = new ArrayList<>();
        for (PurchaseOrderInfo info : purchaseOrderInfos) {
            PurchaseOrderSummary summary = new PurchaseOrderSummary();
            summary.id = info.id;
            summary.date = info.date;
            summary.status = info.status;
            summary.total = info.total;
            summaries.add(summary);
        }
        return summaries;
    }
}
