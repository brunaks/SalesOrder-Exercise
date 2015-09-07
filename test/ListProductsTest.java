import Entities.Product.ProductInfo;
import UseCases.Product.ListProductsUseCase;
import UseCases.Product.RegisterProductUseCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by i848075 on 28/08/2015.
 */
public class ListProductsTest {

    private ListProductsUseCase list;
    private FakeProductRepository repository;
    private ArrayList<ProductInfo> listOfProducts;

    @Before
    public void setUp() {
        repository = new FakeProductRepository();
        list = new ListProductsUseCase(repository);
    }

    @Test
    public void thereAreNoProductsRegistered_ListIsEmpty() {
        whenRetrievingList();
        itShouldHaveNItems(0);
    }

    @Test
    public void givenProducts_theListShouldReturnThen_withTheCorrectData() {
        givenProduct("name1", "description1", 10, 10);
        givenProduct("name2", "description2", 20, 20);
        whenRetrievingList();
        itShouldHaveNItems(2);
        andItShouldDisplayTheData(0, "name1", "description1", 10, 10);
        andItShouldDisplayTheData(1, "name2", "description2", 20, 20);
    }

    private void whenRetrievingList() {listOfProducts = list.returnsAllProducts();}

    private void itShouldHaveNItems(int quantity) {assertEquals(listOfProducts.size(), quantity);}

    private void andItShouldDisplayTheData(int index, String name, String description, int price, int unitsInStock) {
        assertEquals(listOfProducts.get(index).name, name);
        assertEquals(listOfProducts.get(index).description, description);
        assertEquals(listOfProducts.get(index).price, price, 0.01);
        assertEquals(listOfProducts.get(index).unitsInStock, unitsInStock);
    }

    private void givenProduct(String name, String description, int price, int unitsInStock) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.name = name;
        productInfo.description = description;
        productInfo.price = price;
        productInfo.unitsInStock = unitsInStock;
        new RegisterProductUseCase(new FakeProductReceiver(), productInfo, repository).execute();
    }
}
