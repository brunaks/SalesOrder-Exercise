package Persistence;

import Entities.FinancialRecords.SumToReceiveInfo;
import Entities.FinancialRecords.TotalToReceiveAndPayInfo;
import Interfaces.Persistence.SumToReceiveRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 31/01/2016.
 */
public class InMemorySumsToReceiveRepository implements SumToReceiveRepository {

    private List sumsToReceive;

    public InMemorySumsToReceiveRepository() {
        this.sumsToReceive = new ArrayList<>();
    }

    @Override
    public List<SumToReceiveInfo> getAll() {
        return sumsToReceive;
    }

    @Override
    public TotalToReceiveAndPayInfo getTotals() {
        return null;
    }

    @Override
    public void save(SumToReceiveInfo sumToReceiveInfo) {
        this.sumsToReceive.add(sumToReceiveInfo);
    }
}
