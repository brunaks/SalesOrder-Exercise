package Routes.Order.SalesOrder;

import Entities.Order.SalesOrderInfo;
import Interfaces.Persistence.CustomerRepository;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.SalesOrderReceiver;
import Routes.RequestObjects.SalesOrderRequest;
import UseCases.Order.Sales.CreateSalesOrderUseCase;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 25/11/2015.
 */
public class CreateSalesOrderRoute implements Route {
    Gson converter = new Gson();
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private SalesOrderReceiver receiver;
    private SalesOrderRepository repository;
    private String id;
    private String customerId;
    private Date order_date;
    private SalesOrderInfo orderInfo;
    private CreateSalesOrderUseCase createOrder;

    public CreateSalesOrderRoute(SalesOrderRepository salesOrderRepository, SalesOrderReceiver salesOrderReceiver, ProductRepository repository, CustomerRepository customerRepository) {
        this.repository = salesOrderRepository;
        this.receiver = salesOrderReceiver;
        this.productRepository = repository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        getRequestInfo(request);
        createOrderInfo();

        createOrder = new CreateSalesOrderUseCase(orderInfo.id,
                this.customerId,
                repository,
                customerRepository,
                receiver,
                this.order_date);
        createOrder.execute();
        return converter.toJson(receiver);
    }

    private void createOrderInfo() {
        this.orderInfo = new SalesOrderInfo();
        orderInfo.id = this.id;
        orderInfo.date = this.order_date;
        orderInfo.customerId = this.customerId;
    }

    private void getRequestInfo(Request rq) {
        SalesOrderRequest input = converter.fromJson(rq.body(), SalesOrderRequest.class);
        this.id = UUID.randomUUID().toString();
        this.customerId = input.customer_id;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.order_date = formatter.parse(input.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
