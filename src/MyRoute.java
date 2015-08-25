import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by i848075 on 25/08/2015.
 */
class MyRoute implements Route {
    public Object handle(Request rq, Response rp) throws Exception {
        String name = rq.queryParams("name");
        return "Hello " + name + "!";
    }
}
