package Routes;

import Interfaces.Persistence.SalesOrderRepository;
import spark.Route;

/**
 * Created by Bruna Koch Schmitt on 29/11/2015.
 */
public class ShowSalesOrderRoute implements Route {
    public ShowSalesOrderRoute(SalesOrderRepository salesOrderRepository) {
    }
}
