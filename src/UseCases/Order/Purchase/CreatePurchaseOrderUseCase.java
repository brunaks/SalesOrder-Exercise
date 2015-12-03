package UseCases.Order.Purchase;

import Entities.Order.OrderItem;
import Entities.Order.PurchaseOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.PurchaseOrderRepository;
import Interfaces.Receivers.PurchaseOrderReceiver;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class CreatePurchaseOrderUseCase {

    private Date date;
    private String id;
    private PurchaseOrderRepository purchaseOrderRepository;
    private PurchaseOrderReceiver receiver;
    private ProductRepository productRepository;
    private ArrayList<OrderItem> items = new ArrayList<OrderItem>();
    private PurchaseOrderInfo purchaseOrderInfo;

    public CreatePurchaseOrderUseCase(String id, PurchaseOrderRepository purchaseOrderRepository, ProductRepository productRepository, PurchaseOrderReceiver receiver, Date date) {
        this.id = id;
        this.date = date;
        this.productRepository = productRepository;
        this.receiver = receiver;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public void execute() {
        PurchaseOrderInfo purchaseOrderInfo = this.createInfoToSave();
        this.purchaseOrderRepository.save(purchaseOrderInfo);
    }

    private PurchaseOrderInfo createInfoToSave() {
        this.purchaseOrderInfo = new PurchaseOrderInfo();
        this.purchaseOrderInfo.id = this.id;
        this.purchaseOrderInfo.date = this.date;
        this.purchaseOrderInfo.items = this.items;
        this.purchaseOrderInfo.total = 0.0;
        this.purchaseOrderInfo.status = PurchaseOrderInfo.OPEN;
        return this.purchaseOrderInfo;
    }

}
