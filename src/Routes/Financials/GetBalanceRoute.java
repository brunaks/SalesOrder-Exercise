package Routes.Financials;

import Interfaces.Persistence.BalanceRepository;
import Interfaces.Persistence.SumToPayRepository;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 03/12/2015.
 */
public class GetBalanceRoute implements Route{
    private BalanceRepository repository;

    public GetBalanceRoute(BalanceRepository balanceRepository) {
        this.repository = balanceRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson converter = new Gson();
        return converter.toJson(this.repository.getBalance());
    }
}
