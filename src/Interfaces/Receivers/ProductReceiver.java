package Interfaces.Receivers;

/**
 * Created by I848075 on 19/08/2015.
 */
public interface ProductReceiver {
    void registrationFailed();
    void registrationWasSuccessful();
    void productInformationIsInvalid();
    void updateFailed();
    void deleteFailed();
}
