import Entities.Order.OrderInfo;
import Interfaces.Persistence.OrderRepository;
import Interfaces.Receivers.OrderReceiver;
import UseCases.Order.CreateOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public class CreateOrderTest {

    OrderReceiver receiver;
    OrderRepository repository;
    CreateOrder createOrder;
    private OrderInfo orderInfo;

    @Before
    public void setUp() throws Exception {
        receiver = new FakeOrderReceiver();
        repository = new InMemoryOrderRepository();
    }

    @Test
    public void canCreateOrderWithSuccess_OneProduct() {
        OrderInfo info = createOrderInfo();
        createOrder = new CreateOrder(repository, receiver, orderInfo);
        Assert.assertFalse(receiver.createOrderFailed());
    }

    private OrderInfo createOrderInfo() {
        OrderInfo info = new OrderInfo();
        return info;
    }
}
