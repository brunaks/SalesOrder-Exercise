package TestDoubles.Receiver;

import Interfaces.Receivers.CustomerReceiver;

/**
 * Created by Bruna Koch Schmitt on 28/11/2015.
 */
public class FakeCustomerReceiver implements CustomerReceiver {
    public boolean registerFailed = false;
    public boolean infoIsInvalid = false;

    @Override
    public void registerFailed() {
        this.registerFailed = true;
    }

    @Override
    public void customerInfoIsInvalid() {
        this.infoIsInvalid = true;
    }

}
