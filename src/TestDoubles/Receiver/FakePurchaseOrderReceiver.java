package TestDoubles.Receiver;

import Interfaces.Receivers.PurchaseOrderReceiver;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class FakePurchaseOrderReceiver implements PurchaseOrderReceiver {
    public boolean createOrderFailed = false;
    public boolean productDoesNotExist = false;
    public boolean deleteFailed = false;
    public boolean statusUpdateFailed = false;
    public boolean orderIdIsInvalid = false;
    public boolean addItemFailed = false;

    @Override
    public void createOrderFailed() {
        createOrderFailed = true;
    }

    @Override
    public void productDoesNotExist() {
        productDoesNotExist = true;
    }

    @Override
    public void deleteFailed() {
        this.deleteFailed = true;
    }

    @Override
    public void orderIdIsInvalid() {
        this.orderIdIsInvalid = true;
    }

    @Override
    public void updateStatusFailed() {
        this.statusUpdateFailed = true;
    }

    @Override
    public void addItemWasSuccessful() {
        this.addItemFailed = false;
    }

    @Override
    public void addItemFailed() {
        this.addItemFailed = true;
    }
}
