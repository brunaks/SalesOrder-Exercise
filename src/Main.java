import Interfaces.Persistence.CustomerRepository;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Interfaces.Receivers.CustomerReceiver;
import Interfaces.Receivers.ProductReceiver;
import Interfaces.Receivers.SalesOrderReceiver;
import Persistence.JDBCCustomerRepository;
import Persistence.JDBCProductRepository;
import Persistence.JDBCSalesOrderRepository;
import Routes.Customer.ListCustomersRoute;
import Routes.Customer.RegisterCustomerRoute;
import Routes.Order.SalesOrder.*;
import Routes.Product.DeleteProductRoute;
import Routes.Product.ListProductsRoute;
import Routes.Product.RegisterProductRoute;
import Routes.Product.UpdateProductRoute;
import TestDoubles.Receiver.FakeCustomerReceiver;
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

        CustomerRepository customerRepository = new JDBCCustomerRepository();
        CustomerReceiver customerReceiver = new FakeCustomerReceiver();

        Spark.externalStaticFileLocation("resources/public");

        Spark.post("/registerProduct", new RegisterProductRoute(productRepository, receiver));
        Spark.get("/products", new ListProductsRoute(productRepository));
        Spark.post("/updateProduct", new UpdateProductRoute(productRepository, receiver));
        Spark.post("/deleteProduct", new DeleteProductRoute(productRepository, receiver));

        Spark.post("/registerCustomer", new RegisterCustomerRoute(customerRepository, customerReceiver));
        Spark.get("/listCustomers", new ListCustomersRoute(customerRepository));

        Spark.post("/createSalesOrder", new CreateSalesOrderRoute(salesOrderRepository, salesOrderReceiver, productRepository, customerRepository));
        Spark.get("/listSalesOrders", new ListSalesOrdersRoute(salesOrderRepository, salesOrderReceiver));
        Spark.get("/showSalesOrder", new ShowSalesOrderRoute(salesOrderRepository));
        Spark.get("/showSalesOrderItems", new ShowSalesOrderItemsRoute(salesOrderRepository));
        Spark.post("/createSalesOrderItem", new CreateSalesOrderItemRoute(salesOrderRepository));
    }
}
