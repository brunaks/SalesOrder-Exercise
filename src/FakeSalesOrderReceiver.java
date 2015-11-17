import Interfaces.Receivers.OrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public class FakeSalesOrderReceiver implements OrderReceiver {

    public boolean orderFailed = false;
    public boolean productDoesNotExist = false;

    @Override
    public void productDoesNotExist() {
        this.productDoesNotExist = true;
    }

    @Override
    public void createOrderFailed() {
        this.orderFailed = true;
    }
}
