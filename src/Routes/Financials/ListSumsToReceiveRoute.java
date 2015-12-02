package Routes.Financials;

import Interfaces.Persistence.SumToReceiveRepository;
import UseCases.Financials.ListSumsToReceiveUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public class ListSumsToReceiveRoute implements Route {

    private SumToReceiveRepository repository;

    public ListSumsToReceiveRoute(SumToReceiveRepository sumToReceiveRepository) {
        this.repository = sumToReceiveRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ListSumsToReceiveUseCase list = new ListSumsToReceiveUseCase(this.repository);
        Gson converter = new Gson();
        return converter.toJson(list.getAll());
    }
}
