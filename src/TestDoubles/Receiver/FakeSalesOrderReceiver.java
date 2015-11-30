package TestDoubles.Receiver;

import Interfaces.Receivers.SalesOrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public class FakeSalesOrderReceiver implements SalesOrderReceiver {

    public boolean createOrderFailed = false;
    public boolean productDoesNotExist = false;
    public boolean customerDoesNotExist = false;
    public boolean salesOrderIdIsInvalid = false;
    public boolean deleteFailed = false;
    public boolean statusUpdateFailed = false;
    public boolean addItemFailed = false;

    @Override
    public void productDoesNotExist() {
        this.productDoesNotExist = true;
    }

    @Override
    public void createOrderFailed() {
        this.createOrderFailed = true;
    }

    @Override
    public void customerDoesNotExist() {
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

    @Override
    public void updateStatusFailed() {
        this.statusUpdateFailed = true;
    }
}
