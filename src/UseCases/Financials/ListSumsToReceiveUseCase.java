package UseCases.Financials;

import Entities.FinancialRecords.SumToReceiveInfo;
import Interfaces.Persistence.SumToReceiveRepository;

import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public class ListSumsToReceiveUseCase {

    private SumToReceiveRepository repository;

    public ListSumsToReceiveUseCase(SumToReceiveRepository repository) {
        this.repository = repository;
    }

    public List<SumToReceiveInfo> getAll() {
        return this.repository.getAll();
    }
}
