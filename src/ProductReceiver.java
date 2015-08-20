/**
 * Created by I848075 on 19/08/2015.
 */
public interface ProductReceiver {
    boolean productWasRegisteredSuccessfully();

    void registrationFailed();

    void RegistrationWasSuccessful();

    void productInformationIsInvalid();

    boolean productFound();
}
