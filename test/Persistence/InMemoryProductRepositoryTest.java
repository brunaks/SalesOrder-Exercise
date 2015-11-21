package Persistence;

import Interfaces.Persistence.ProductRepository;
import TestDoubles.Persistence.InMemoryProductRepository;

/**
 * Created by I848075 on 21/08/2015.
 */
public class InMemoryProductRepositoryTest extends ProductRepositoryTest{

    @Override
    protected ProductRepository createRepository() {return new InMemoryProductRepository();}
}
