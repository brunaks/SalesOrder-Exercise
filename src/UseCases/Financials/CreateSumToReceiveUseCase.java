package UseCases.Financials;

import Entities.FinancialRecords.SumToReceiveInfo;
import Interfaces.Persistence.SumToReceiveRepository;

/**
 * Created by Bruna Koch Schmitt on 31/01/2016.
 */
public class CreateSumToReceiveUseCase {

    private SumToReceiveRepository sumToReceiveRepository;

    public CreateSumToReceiveUseCase(SumToReceiveRepository sumToReceiveRepository) {
        this.sumToReceiveRepository = sumToReceiveRepository;
    }

    public void execute(SumToReceiveInfo sumToReceiveInfo) {
        this.sumToReceiveRepository.save(sumToReceiveInfo);
    }
}
