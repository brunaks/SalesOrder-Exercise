import Interfaces.Receivers.OrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public class FakeOrderReceiver implements OrderReceiver {
    @Override
    public boolean createOrderFailed() {
        return false;
    }
}
