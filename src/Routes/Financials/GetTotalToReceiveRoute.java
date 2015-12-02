package Routes.Financials;

import Interfaces.Persistence.SumToReceiveRepository;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public class GetTotalToReceiveRoute implements Route {

    private SumToReceiveRepository repository;

    public GetTotalToReceiveRoute(SumToReceiveRepository sumToReceiveRepository) {
        this.repository = sumToReceiveRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson converter = new Gson();
        return converter.toJson(this.repository.getTotals());
    }
}
