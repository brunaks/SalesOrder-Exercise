package UseCases.Order;

import Entities.Order.PurchaseOrderInfo;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Persistence.InMemoryPurchaseOrderRepository;
import TestDoubles.Receiver.FakePurchaseOrderReceiver;
import UseCases.Order.Purchase.CreatePurchaseOrderUseCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class CreatePurchaseOrderTest {

    FakePurchaseOrderReceiver receiver;
    InMemoryPurchaseOrderRepository purchaseOrderRepository;
    CreatePurchaseOrderUseCase createOrder;
    InMemoryProductRepository productRepository;

    @Before
    public void setUp() throws Exception {
        receiver = new FakePurchaseOrderReceiver();
        purchaseOrderRepository = new InMemoryPurchaseOrderRepository();
        productRepository = new InMemoryProductRepository();
    }

    @Test
    public void canCreateOrderWithSuccess() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();

        createOrder = new CreatePurchaseOrderUseCase(id, purchaseOrderRepository, productRepository, receiver, date);
        createOrder.execute();

        ReadPurchaseOrderUseCase readOrder = new ReadPurchaseOrderUseCase(purchaseOrderRepository);
        PurchaseOrderInfo info = readOrder.withId(id);

        Assert.assertNotNull(info);
        Assert.assertEquals(id, info.id);
        Assert.assertEquals(date, info.date);
        Assert.assertEquals(PurchaseOrderInfo.OPEN, info.status);
        Assert.assertEquals(0.0, info.total, 0.01);
        Assert.assertEquals(0, info.items.size());
        Assert.assertFalse(this.receiver.createOrderFailed);
        Assert.assertFalse(this.receiver.orderIdIsInvalid);
    }

    private Date givenDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

}
