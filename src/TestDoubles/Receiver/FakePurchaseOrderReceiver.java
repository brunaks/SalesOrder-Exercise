package TestDoubles.Receiver;

import Interfaces.Receivers.PurchaseOrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class FakePurchaseOrderReceiver implements PurchaseOrderReceiver {
    public boolean orderFailed = false;
    public boolean productDoesNotExist = false;
    public boolean deleteFailed = false;
    public boolean OrderIdIsInvalid = false;

    @Override
    public void createOrderFailed() {
        orderFailed = true;
    }

    @Override
    public void productDoesNotExist() {
        productDoesNotExist = true;
    }
}
