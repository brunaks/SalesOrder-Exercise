package Persistence;

import Entities.FinancialRecords.TotalToReceiveAndPayInfo;
import Interfaces.Persistence.BalanceRepository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by Bruna Koch Schmitt on 03/12/2015.
 */
public class JDBCBalanceRepository implements BalanceRepository {

    private final Connection connection;

    public JDBCBalanceRepository() {
        connection = OracleConnectionFactory.getConnection();
    }

    @Override
    public TotalToReceiveAndPayInfo getBalance() {
        String sql = "{? = call return_balance()}";
        ResultSet result;
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.NUMBER);
            stmt.executeQuery();
            Double balance = stmt.getDouble(1);
            TotalToReceiveAndPayInfo balanceInfo = new TotalToReceiveAndPayInfo();
            balanceInfo.balance = balance;
            return balanceInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

