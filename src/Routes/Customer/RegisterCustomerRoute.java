package Routes.Customer;

import Entities.Customer.CustomerInfo;
import Interfaces.Persistence.CustomerRepository;
import Interfaces.Receivers.CustomerReceiver;
import Routes.RequestObjects.CustomerInfoRequest;
import UseCases.Customer.RegisterCustomerUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 28/11/2015.
 */
public class RegisterCustomerRoute implements Route {

    Gson converter = new Gson();
    private CustomerReceiver receiver;
    private CustomerRepository repository;
    private CustomerInfoRequest customerInfoRequest;
    private CustomerInfo info = new CustomerInfo();

    public RegisterCustomerRoute(CustomerRepository customerRepository, CustomerReceiver customerReceiver) {
        this.repository = customerRepository;
        this.receiver = customerReceiver;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        getRequestInfo(request);
        RegisterCustomerUseCase registerCustomer = new RegisterCustomerUseCase(this.repository, this.receiver, this.info);
        registerCustomer.execute();
        return converter.toJson(this.receiver);
    }

    private void getRequestInfo(Request request) {
        this.customerInfoRequest = converter.fromJson(request.body(), CustomerInfoRequest.class);
        this.info.id = this.repository.generateId();
        this.info.name = customerInfoRequest.name;
        this.info.cpf = customerInfoRequest.cpf;
        this.info.telephoneNumber = customerInfoRequest.telephoneNumber;
        this.info.address = customerInfoRequest.address;
    }
}
