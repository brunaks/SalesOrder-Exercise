import Entities.Customer.CustomerInfo;
import Interfaces.Persistence.CustomerRepository;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.ProductReceiver;
import Interfaces.Receivers.SalesOrderReceiver;
import Persistence.JDBCProductRepository;
import Persistence.JDBCSalesOrderRepository;
import Routes.Order.ListSalesOrdersRoute;
import Routes.Order.SalesOrder.CreateSalesOrderItemRoute;
import Routes.Order.SalesOrder.CreateSalesOrderRoute;
import Routes.Order.SalesOrder.ShowSalesOrderItemsRoute;
import Routes.Order.SalesOrder.ShowSalesOrderRoute;
import Routes.ProductRoutes.DeleteProductRoute;
import Routes.ProductRoutes.ListProductsRoute;
import Routes.ProductRoutes.RegisterProductRoute;
import Routes.ProductRoutes.UpdateProductRoute;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import TestDoubles.Receiver.FakeProductReceiver;
import TestDoubles.Receiver.FakeSalesOrderReceiver;
import spark.Spark;

/**
 * Created by i848075 on 25/08/2015.
 */
public class Main {

    public static void main(String[] args) {
        ProductReceiver receiver = new FakeProductReceiver();
        ProductRepository productRepository = new JDBCProductRepository();
        SalesOrderRepository salesOrderRepository = new JDBCSalesOrderRepository(productRepository);
        SalesOrderReceiver salesOrderReceiver = new FakeSalesOrderReceiver();

        CustomerRepository customerRepository = new InMemoryCustomerRepository();
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.id = "dd69cb36-93d7-11e5-8994-feff819cdc9f";
        customerRepository.saveCustomer(customerInfo);

        Spark.externalStaticFileLocation("resources/public");

        Spark.post("/registerProduct", new RegisterProductRoute(productRepository, receiver));
        Spark.get("/products", new ListProductsRoute(productRepository));
        Spark.post("/updateProduct", new UpdateProductRoute(productRepository, receiver));
        Spark.post("/deleteProduct", new DeleteProductRoute(productRepository, receiver));

        Spark.post("/createSalesOrder", new CreateSalesOrderRoute(salesOrderRepository, salesOrderReceiver, productRepository, customerRepository));
        Spark.get("/listSalesOrders", new ListSalesOrdersRoute(salesOrderRepository, salesOrderReceiver));
        Spark.get("/showSalesOrder", new ShowSalesOrderRoute(salesOrderRepository));
        Spark.get("/showSalesOrderItems", new ShowSalesOrderItemsRoute(salesOrderRepository));
        Spark.post("/createSalesOrderItem", new CreateSalesOrderItemRoute(salesOrderRepository));
    }
}
