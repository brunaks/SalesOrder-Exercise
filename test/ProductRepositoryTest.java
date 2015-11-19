import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 12/09/2015.
 */
public abstract class ProductRepositoryTest {
    ProductRepository repository;
    FakeProductReceiver receiver;

    @Before
    public void setUp() throws Exception {
        receiver = new FakeProductReceiver();
        repository = createRepository();
    }

    protected abstract ProductRepository createRepository();

    @Test
    public void canSaveAndReadOneProduct() {
        ProductInfo productInfo = givenProductInfo(UUID.randomUUID().toString(), "productName", "productDescription", 10.0, 10);
        repository.saveProduct(productInfo);
        ProductInfo productRetrieved = repository.getProductInfoByName("productName");
        assertProductsInfoAreEqual(productInfo, productRetrieved);
    }

    @Test
    public void canSaveAndReadTwoProducts() {
        ProductInfo productInfo = givenProductInfo(UUID.randomUUID().toString(), "productName", "productDescription", 10.0, 10);
        repository.saveProduct(productInfo);
        ProductInfo productInfoRetrieved = repository.getProductInfoByName("productName");
        assertProductsInfoAreEqual(productInfo, productInfoRetrieved);

        ProductInfo productInfo2 = givenProductInfo(UUID.randomUUID().toString(), "productName2", "productDescription2", 20.0, 20);
        repository.saveProduct(productInfo2);
        ProductInfo productInfoRetrieved2 = repository.getProductInfoByName("productName2");
        assertProductsInfoAreEqual(productInfo2, productInfoRetrieved2);
    }

    @Test
    public void changesInTheExternalProductObjectAreNotReflectedInProductSaved() {
        ProductInfo productInfo = givenProductInfo(UUID.randomUUID().toString(), "productName", "productDescription", 10.0, 10);
        repository.saveProduct(productInfo);
        ProductInfo productInfoRetrieved = repository.getProductInfoByName("productName");
        assertProductsInfoAreEqual(productInfo, productInfoRetrieved);

        productInfo.name = "productNameChanged";
        Assert.assertNotEquals(productInfo.name, productInfoRetrieved.name);
    }

    @Test
    public void canUpdateOneProduct() {
        ProductInfo productInfo = givenProductInfo(UUID.randomUUID().toString(), "productName", "productDescription", 10.0, 10);
        repository.saveProduct(productInfo);
        ProductInfo productInfoRetrieved = repository.getProductInfoByName("productName");
        assertProductsInfoAreEqual(productInfo, productInfoRetrieved);

        ProductInfo newInfo = createProductInfo("productNameChanged", "productDescriptionChanged", 20.0, 20);
        repository.updateProduct(productInfoRetrieved.id, newInfo);
        ProductInfo productInfoUpdated = repository.getProductInfoById(productInfoRetrieved.id);
        assertProductsInfoAreEqual(newInfo, productInfoUpdated);
    }

    @Test
    public void canDeleteOneProduct() {
        ProductInfo productInfo = givenProductInfo(UUID.randomUUID().toString(), "productName", "productDescription", 10.0, 10);
        repository.saveProduct(productInfo);
        Assert.assertEquals(1, repository.getAllProductsInfoSaved().size());

        ProductInfo productInfoRetrieved = repository.getProductInfoByName("productName");
        repository.deleteProductWithId(productInfoRetrieved.id);
        Assert.assertEquals(0, repository.getAllProductsInfoSaved().size());
    }

    private ProductInfo createProductInfo(String name, String description, double price, int unitsInStock) {
        ProductInfo info = new ProductInfo();
        info.name = name;
        info.description = description;
        info.price = price;
        info.unitsInStock = unitsInStock;
        return info;
    }

    protected ProductInfo givenProductInfo(String ID, String productName, String productDescription, double price, int unitsInStock) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.name = productName;
        productInfo.description = productDescription;
        productInfo.price = price;
        productInfo.unitsInStock = unitsInStock;
        productInfo.id = ID;
        return productInfo;
    }

    private void assertProductsInfoAreEqual(ProductInfo productInfo, ProductInfo productInfoRetrieved) {
        Assert.assertEquals(productInfo.name, productInfoRetrieved.name);
        Assert.assertEquals(productInfo.description, productInfoRetrieved.description);
        Assert.assertEquals(productInfo.price, productInfoRetrieved.price, 0.001);
        Assert.assertEquals(productInfo.unitsInStock, productInfoRetrieved.unitsInStock);
    }
}
