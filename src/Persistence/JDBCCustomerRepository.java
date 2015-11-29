package Persistence;

import Entities.Customer.CustomerInfo;
import Interfaces.Persistence.CustomerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 29/11/2015.
 */
public class JDBCCustomerRepository implements CustomerRepository {

    private Connection connection;

    public JDBCCustomerRepository() {
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public void saveCustomer(CustomerInfo customerInfo) {
        String sql = "insert into customer " +
                "(id, name, cpf, phoneNumber, address)" +
                " values (?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, customerInfo.id);
            stmt.setString(2, customerInfo.name);
            stmt.setString(3, customerInfo.cpf);
            stmt.setString(4, customerInfo.telephoneNumber);
            stmt.setString(5, customerInfo.address);

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomerInfo getCustomerById(String customerID) {
        String sql = "select * from customer where id = ?";
        ResultSet result;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customerID);
            result = stmt.executeQuery();
            if (result.next() == false) {
                return null;
            } else {
                return buildCustomerInfo(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CustomerInfo buildCustomerInfo(ResultSet result) {
        CustomerInfo info = new CustomerInfo();
        try {
            do {
                info.id = result.getString("id");
                info.name = result.getString("name");
                info.cpf = result.getString("cpf");
                info.telephoneNumber = result.getString("phoneNumber");
                info.address = result.getString("address");
            } while (result.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    public List<CustomerInfo> getAll() {
        String sql = "select * from customer";
        ResultSet result;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            result = stmt.executeQuery();
            return buildCustomerInfos(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<CustomerInfo> buildCustomerInfos(ResultSet result) {
        ArrayList<CustomerInfo> infos = new ArrayList<>();
        try {
            while (result.next()) {
                CustomerInfo info = new CustomerInfo();
                info.id = result.getString("id");
                info.name = result.getString("name");
                info.cpf = result.getString("cpf");
                info.telephoneNumber = result.getString("phoneNumber");
                info.address = result.getString("address");
                infos.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infos;
    }

    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
