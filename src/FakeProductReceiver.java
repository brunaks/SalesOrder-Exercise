/**
 * Created by I848075 on 19/08/2015.
 */
public class FakeProductReceiver implements ProductReceiver {

    private boolean registrationWasSuccessful;
    private boolean productWasSavedSucessfully;
    private boolean productIsInRepository;

    @Override
    public boolean productWasRegisteredSuccessfully() {
        return registrationWasSuccessful;
    }

    @Override
    public void registrationFailed() {
        this.registrationWasSuccessful = false;
    }

    @Override
    public void registrationWasSuccessful() {
        this.registrationWasSuccessful = true;
    }

    @Override
    public void productInformationIsInvalid() {

    }

    @Override
    public void productFound() {
        productIsInRepository = true;
    }

    @Override
    public void productWasNotFound() {
        productIsInRepository = false;
    }

    @Override
    public boolean productIsInRepository() {
        return productIsInRepository;
    }

    @Override
    public void productWasNotSaved() {
        this.productWasSavedSucessfully = false;
    }

    @Override
    public boolean productWasSavedSuccessfully() {
        return this.productWasSavedSucessfully;
    }

    @Override
    public void productWasSaved() {
        this.productWasSavedSucessfully = true;
    }

}
