package Persistence;

import Entities.FinancialRecords.SumToPayInfo;
import Entities.FinancialRecords.TotalToReceiveAndPayInfo;
import Interfaces.Persistence.SumToPayRepository;
import Interfaces.Persistence.SumToReceiveRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public class JDBCSumToPayRepository implements SumToPayRepository {

    private final Connection connection;

    public JDBCSumToPayRepository() {
        connection = ConnectionFactory.getConnection();
    }

    @Override
    public List<SumToPayInfo> getAll() {
        String sql = "select * from sum_to_pay";
        ResultSet result;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            result = stmt.executeQuery();
            return buildSumInfos(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<SumToPayInfo> buildSumInfos(ResultSet result) {
        ArrayList<SumToPayInfo> infos = new ArrayList<>();
        try {
            while (result.next()) {
                SumToPayInfo info = new SumToPayInfo();
                info.sum_id = result.getString("sumId");
                info.pay_date = result.getString("payDate");
                info.pay_status = result.getString("payStatus");
                info.sum_to_pay = result.getDouble("sum_to_pay");
                info.order_id = result.getString("orderId");
                infos.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infos;
    }

    @Override
    public TotalToReceiveAndPayInfo getTotals() {
        String sql = "{? = call return_total_to_pay()}";
        ResultSet result;
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.NUMBER);
            stmt.executeQuery();
            Double total = stmt.getDouble(1);
            TotalToReceiveAndPayInfo totalToPay = new TotalToReceiveAndPayInfo();
            totalToPay.totalToPay = total;
            return totalToPay;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
