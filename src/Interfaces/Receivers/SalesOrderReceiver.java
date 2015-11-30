package Interfaces.Receivers;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public interface SalesOrderReceiver {
    void productDoesNotExist();
    void createOrderFailed();

    void customerDoesNotExist();
    void salesOrderIdIsInvalid();
    void deleteFailed();
    void updateStatusFailed();
}
