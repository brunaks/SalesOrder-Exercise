package Interfaces.Receivers;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public interface PurchaseOrderReceiver {
    void createOrderFailed();
    void productDoesNotExist();
}
