package Interfaces.Receivers;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public interface OrderReceiver {
    void productDoesNotExist();
    void createOrderFailed();
    void clientDoesNotExist();
}
