package UseCases.Order;

import Entities.Order.PurchaseOrderInfo;
import Interfaces.Persistence.PurchaseOrderRepository;
import TestDoubles.Persistence.InMemoryPurchaseOrderRepository;

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
}
