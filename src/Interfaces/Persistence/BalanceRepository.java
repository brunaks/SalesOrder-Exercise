package Interfaces.Persistence;

import Entities.FinancialRecords.TotalToReceiveAndPayInfo;

/**
 * Created by Bruna Koch Schmitt on 03/12/2015.
 */
public interface BalanceRepository {
    TotalToReceiveAndPayInfo getBalance();
}
