import Interfaces.Receivers.SalesOrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public class FakeSalesOrderReceiver implements SalesOrderReceiver {

    public boolean orderFailed = false;
    public boolean productDoesNotExist = false;
    public boolean customerDoesNotExist = false;
    public boolean salesOrderIdIsInvalid = false;
    public boolean deleteFailed = false;

    @Override
    public void productDoesNotExist() {
        this.productDoesNotExist = true;
    }

    @Override
    public void createOrderFailed() {
        this.orderFailed = true;
    }

    @Override
    public void clientDoesNotExist() {
        this.customerDoesNotExist = true;
    }

    @Override
    public void salesOrderIdIsInvalid() {
        this.salesOrderIdIsInvalid = true;
    }

    @Override
    public void deleteFailed() {
        this.deleteFailed = true;
    }
}
