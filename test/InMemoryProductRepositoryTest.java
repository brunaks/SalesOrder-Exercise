import Entities.Product.Product;
import Interfaces.Persistence.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by I848075 on 21/08/2015.
 */
public class InMemoryProductRepositoryTest extends ProductRepositoryTest{

    @Override
    protected ProductRepository createRepository() {return new InMemoryProductRepository();}
}
