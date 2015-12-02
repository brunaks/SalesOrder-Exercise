package Routes.Financials;

import Interfaces.Persistence.SumToPayRepository;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public class GetTotalToPayRoute implements Route {
    private SumToPayRepository repository;

    public GetTotalToPayRoute(SumToPayRepository sumToPayRepository) {
        this.repository = sumToPayRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson converter = new Gson();
        return converter.toJson(this.repository.getTotals());
    }
}
