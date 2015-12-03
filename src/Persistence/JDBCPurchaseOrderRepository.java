package Persistence;

import Routes.RequestObjects.CreatePurchaseOrderItemRequest;
import Entities.Order.OrderItem;
import Entities.Order.PurchaseOrderInfo;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import Interfaces.Persistence.PurchaseOrderRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 03/12/2015.
 */
public class JDBCPurchaseOrderRepository implements PurchaseOrderRepository {

    private ProductRepository productRepository;
    private Connection connection;

    public JDBCPurchaseOrderRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
        connection = ConnectionFactory.getConnection();
    }

    @Override
    public List<PurchaseOrderInfo> getAll() {
        String sql = "select * from purchase_order";
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

    private List<PurchaseOrderInfo> buildOrderInfos(ResultSet result) {
        ArrayList<PurchaseOrderInfo> infos = new ArrayList<>();
        try {
            do
                infos.add(buildOrderInfo(result));
            while (result.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infos;
    }

    private PurchaseOrderInfo buildOrderInfo(ResultSet result) throws SQLException {
        PurchaseOrderInfo info = new PurchaseOrderInfo();
        info.id = result.getString("order_id");
        info.status = result.getString("order_status");
        info.date = result.getDate("order_date");
        info.items = getItems(info.id);
        info.total = getTotal(info.id);
        return info;
    }

    private double getTotal(String id) {
        String sql = "{? = call calculate_purchase_order_total(?)}";
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
        String sql = "select * from items_purchase_order where order_id = ?";
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
    public void save(PurchaseOrderInfo purchaseOrderInfo) {
        String sql = "BEGIN create_purchase_order (?, ?); end;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, purchaseOrderInfo.id);
            stmt.setDate(2, new Date(purchaseOrderInfo.date.getTime()));
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PurchaseOrderInfo getById(String id) {
        String sql = "select * from purchase_order where order_id = ?";
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
    public void removeWithId(String id) {

    }

    @Override
    public void updateStatus(String id, String newStatus) {
        String sql = "BEGIN set_purchase_to_in_process (?); end;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createItem(CreatePurchaseOrderItemRequest request) {
        String sql = "insert into items_purchase_order " +
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
    public void addItem(PurchaseOrderInfo order, OrderItem item) {
        //String sql = "insert into items_purchase_order " +
        //       "(order_id, product_id, quantity)" +
        //       " values (?,?,?)";
        String sql = "BEGIN insert_purchase_order_item (?, ?, ?); end;";
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
