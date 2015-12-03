package Routes.Customer;

import Interfaces.Persistence.CustomerRepository;
import UseCases.Customer.ListCustomersUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 28/11/2015.
 */
public class ListCustomersRoute implements Route {

    private CustomerRepository repository;

    public ListCustomersRoute(CustomerRepository customerRepository) {
        this.repository = customerRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ListCustomersUseCase list = new ListCustomersUseCase(this.repository);
        Gson converter = new Gson();
        return converter.toJson(list.getAll());
    }
}
