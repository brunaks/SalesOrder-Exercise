package Persistence;

import Entities.FinancialRecords.SumToPayInfo;
import Entities.FinancialRecords.TotalToReceiveAndPayInfo;
import Interfaces.Persistence.SumToPayRepository;

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
                info.sumId = result.getString("sum_id");
                info.payDate = result.getString("pay_date");
                info.payStatus = result.getString("pay_status");
                info.sumToPay = result.getDouble("sum_to_pay");
                info.orderId = result.getString("order_id");
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
