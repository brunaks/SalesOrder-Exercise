package Persistence;

import Entities.FinancialRecords.SumToReceiveInfo;
import Entities.FinancialRecords.TotalToReceiveAndPayInfo;
import Interfaces.Persistence.SumToReceiveRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public class JDBCSumToReceiveRepository implements SumToReceiveRepository {

    private final Connection connection;

    public JDBCSumToReceiveRepository() {
        connection = ConnectionFactory.getConnection();
    }

    @Override
    public List<SumToReceiveInfo> getAll() {
        String sql = "select * from sum_to_receive";
        ResultSet result;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            result = stmt.executeQuery();
            return buildSumInfos(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TotalToReceiveAndPayInfo getTotals() {
        String sql = "{? = call return_total_to_receive()}";
        ResultSet result;
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.NUMBER);
            stmt.executeQuery();
            Double total = stmt.getDouble(1);
            TotalToReceiveAndPayInfo totalToReceive = new TotalToReceiveAndPayInfo();
            totalToReceive.totalToReceive = total;
            return totalToReceive;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<SumToReceiveInfo> buildSumInfos(ResultSet result) {
        ArrayList<SumToReceiveInfo> infos = new ArrayList<>();
        try {
            while (result.next()) {
                SumToReceiveInfo info = new SumToReceiveInfo();
                info.sum_id = result.getString("sum_id");
                info.pay_date = result.getString("pay_date");
                info.pay_status = result.getString("pay_status");
                info.sum_with_deduction = result.getDouble("sum_with_deduction");
                info.order_id = result.getString("order_id");
                infos.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infos;
    }
}
