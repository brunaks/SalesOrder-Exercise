package Persistence;

import Entities.Customer.CustomerInfo;
import Entities.Order.SalesOrderInfo;
import Interfaces.Persistence.SalesOrderRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 25/11/2015.
 */
public class JDBCSalesOrderRepository implements SalesOrderRepository {

    private final Connection connection;

    public JDBCSalesOrderRepository() {
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
            while (result.next()) {
                SalesOrderInfo info = new SalesOrderInfo();
                info.id = result.getString("id");
                info.status = result.getString("status");
                info.date = result.getDate("order_date");
                info.customerInfo = new CustomerInfo();
                info.customerInfo.id = result.getString("customer_id");
                infos.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infos;
    }

    @Override
    public void save(SalesOrderInfo salesOrderInfo) {
        String sql = "BEGIN create_sales_order (?, ?, ?); end;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, salesOrderInfo.id);
            stmt.setDate(2, new Date(salesOrderInfo.date.getTime()));
            stmt.setString(3, salesOrderInfo.customerInfo.id);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SalesOrderInfo getById(String id) {
        return null;
    }

    @Override
    public void deleteWithId(String id) {

    }

    @Override
    public void updateStatus(String id, String newStatus) {

    }
}
