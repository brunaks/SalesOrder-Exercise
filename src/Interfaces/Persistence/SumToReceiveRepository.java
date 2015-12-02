package Interfaces.Persistence;

import Entities.FinancialRecords.SumToPayInfo;
import Entities.FinancialRecords.SumToReceiveInfo;
import Entities.FinancialRecords.TotalToReceiveAndPayInfo;

import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public interface SumToReceiveRepository {
    List<SumToReceiveInfo> getAll();

    TotalToReceiveAndPayInfo getTotals();
}
