/**
 * Created by I848075 on 19/08/2015.
 */
public interface ProductReceiver {
    boolean productWasRegisteredSuccessfully();

    void registrationFailed();

    void registrationWasSuccessful();

    void productInformationIsInvalid();

    void productFound();

    boolean productIsInRepository();

    void productWasNotSaved();

    boolean productWasSavedSuccessfully();

    void productWasSaved();

    void productWasNotFound();
}
