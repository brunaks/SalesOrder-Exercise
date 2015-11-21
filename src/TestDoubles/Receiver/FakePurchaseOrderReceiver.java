package TestDoubles.Receiver;

import Interfaces.Receivers.PurchaseOrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class FakePurchaseOrderReceiver implements PurchaseOrderReceiver {
    public boolean orderFailed;
}
