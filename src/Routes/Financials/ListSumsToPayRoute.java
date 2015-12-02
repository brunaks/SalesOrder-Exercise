package Routes.Financials;

import Interfaces.Persistence.SumToPayRepository;
import UseCases.Financials.ListSumsToPayUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public class ListSumsToPayRoute implements Route {
    private SumToPayRepository repository;

    public ListSumsToPayRoute(SumToPayRepository sumToPayRepository) {
        this.repository = sumToPayRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ListSumsToPayUseCase list = new ListSumsToPayUseCase(this.repository);
        Gson converter = new Gson();
        return converter.toJson(list.getAll());
    }
}
