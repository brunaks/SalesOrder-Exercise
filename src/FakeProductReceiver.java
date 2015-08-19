/**
 * Created by I848075 on 19/08/2015.
 */
public class FakeProductReceiver implements ProductReceiver{

    private boolean registrationWasSuccessful;

    @Override
    public boolean productWasRegisteredSuccessfully() {
        return registrationWasSuccessful;
    }

    @Override
    public void registrationFailed() {
        this.registrationWasSuccessful = false;
    }

    @Override
    public void RegistrationWasSuccessful() {
        this.registrationWasSuccessful = true;
    }

    @Override
    public void productInformationIsInvalid() {

    }
}
