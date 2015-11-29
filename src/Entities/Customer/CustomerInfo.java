package Entities.Customer;

/**
 * Created by Bruna Koch Schmitt on 16/11/2015.
 */
public class CustomerInfo {
    public String id;
    public String name;
    public String cpf;
    public String telephoneNumber;
    public String address;

    public boolean isValid() {
        if (this.idIsValid() && this.nameIsValid() && this.cpfIsValid() && telephoneNumberIsValid() && this.addressIsValid()) {
            return true;
        } else {
            return false;
        }
    }


    private boolean nameIsValid() {
        return !this.name.trim().isEmpty();
    }

    private boolean idIsValid() {
        return !this.id.trim().isEmpty();
    }

    private boolean cpfIsValid() {
        return !this.cpf.trim().isEmpty() && this.cpf.trim().length() == 11;
    }

    private boolean telephoneNumberIsValid() {
        return !this.telephoneNumber.trim().isEmpty() && this.telephoneNumber.trim().length() == 10;
    }

    private boolean addressIsValid() {
        return !this.address.trim().isEmpty();
    }
}
