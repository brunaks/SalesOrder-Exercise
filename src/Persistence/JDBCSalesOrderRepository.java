package Persistence;

import Entities.Order.OrderItem;
import Entities.Order.SalesOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.SalesOrderRepository;
import Routes.RequestObjects.CreateSalesOrderItemRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 25/11/2015.
 */
public class JDBCSalesOrderRepository implements SalesOrderRepository {
    private ProductRepository productRepository;
    private Connection connection;

    public JDBCSalesOrderRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
        connection = ConnectionFactory.getConnection();
    }

    @Override
    public List<SalesOrderInfo> getAll() {
        String sql = "select * from sales_order";
        ResultSet result;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            result = stmt.executeQuery();
            if (result.next() == false) {
                return null;
            } else {
                return buildOrderInfos(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<SalesOrderInfo> buildOrderInfos(ResultSet result) {
        ArrayList<SalesOrderInfo> infos = new ArrayList<>();
        try {
            do
                infos.add(buildOrderInfo(result));
            while (result.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infos;
    }

    private SalesOrderInfo buildOrderInfo(ResultSet result) throws SQLException {
        SalesOrderInfo info = new SalesOrderInfo();
        info.id = result.getString("order_id");
        info.status = result.getString("order_status");
        info.date = result.getDate("order_date");
        info.customerId = result.getString("customer_id");
        info.items = getItems(info.id);
        info.total = getTotal(info.id);
        return info;
    }

    private double getTotal(String id) {
        String sql = "{? = call calculate_sales_order_total(?)}";
        ResultSet result;
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.NUMBER);
            stmt.setString(2, id);
            stmt.executeQuery();
            return stmt.getDouble(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<OrderItem> getItems(String id) {
        String sql = "select * from items_sales_order where order_id = ?";
        ResultSet result;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            result = stmt.executeQuery();
            if (result.next()) {
                return buildItems(result);
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<OrderItem> buildItems(ResultSet result) {
        ArrayList<OrderItem> items = new ArrayList<>();
        try {
            do
                items.add(buildItem(result));
            while (result.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    private OrderItem buildItem(ResultSet result) throws SQLException {
        ProductInfo productInfo = productRepository.getProductInfoById(result.getString("product_id"));
        return new OrderItem(productInfo, result.getInt("quantity"));
    }

    @Override
    public void save(SalesOrderInfo salesOrderInfo) {
        String sql = "BEGIN create_sales_order (?, ?, ?); end;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, salesOrderInfo.id);
            stmt.setDate(2, new Date(salesOrderInfo.date.getTime()));
            stmt.setString(3, salesOrderInfo.customerId);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SalesOrderInfo getById(String id) {
        String sql = "select * from sales_order where order_id = ?";
        ResultSet result;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            result = stmt.executeQuery();
            if (result.next() == false) {
                return null;
            } else {
                return buildOrderInfo(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteWithId(String id) {
    }

    @Override
    public void updateStatus(String id, String newStatus) {
        String sql = "BEGIN set_sales_order_to_in_process (?); end;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createItem(CreateSalesOrderItemRequest request) {
        String sql = "insert into items_sales_order " +
                "(order_id, product_id, quantity)" +
                " values (?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ProductInfo productInfo = productRepository.getProductInfoByName(request.productName);
            stmt.setString(1, request.orderId);
            stmt.setString(2, productInfo.id);
            stmt.setDouble(3, request.quantity);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addItem(SalesOrderInfo order, OrderItem item) {
        String sql = "insert into items_sales_order " +
                "(order_id, product_id, quantity)" +
                " values (?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ProductInfo productInfo = productRepository.getProductInfoById(item.productInfo.id);
            stmt.setString(1, order.id);
            stmt.setString(2, productInfo.id);
            stmt.setDouble(3, item.quantity);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
