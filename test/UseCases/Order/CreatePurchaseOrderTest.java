package UseCases.Order;

import Entities.Product.ProductInfo;
import TestDoubles.Persistence.InMemoryCustomerRepository;
import TestDoubles.Persistence.InMemoryProductRepository;
import TestDoubles.Persistence.InMemoryPurchaseOrderRepository;
import TestDoubles.Receiver.FakeProductReceiver;
import TestDoubles.Receiver.FakePurchaseOrderReceiver;
import UseCases.Product.RegisterProductUseCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Bruna Koch Schmitt on 19/11/2015.
 */
public class CreatePurchaseOrderTest {

    FakePurchaseOrderReceiver receiver;
    InMemoryPurchaseOrderRepository purchaseOrderRepository;
    CreatePurchaseOrderUseCase createPurchaseOrder;
    InMemoryProductRepository productRepository;
    InMemoryCustomerRepository customerRepository;

    @Before
    public void setUp() throws Exception {
        receiver = new FakePurchaseOrderReceiver();
        purchaseOrderRepository = new InMemoryPurchaseOrderRepository();
        productRepository = new InMemoryProductRepository();
        customerRepository = new InMemoryCustomerRepository();
    }

    @Test
    public void canCreateOrderWithSuccess_OneProduct() {
        Date date = givenDate("01/01/2015");
        String id = UUID.randomUUID().toString();
        createPurchaseOrder = new CreatePurchaseOrderUseCase(id, purchaseOrderRepository, productRepository, receiver, date);
        ProductInfo productInfo = givenProductInfo("Name", "Description", 10, 10);
        createPurchaseOrder.addProduct(productInfo.id, 1);
        createPurchaseOrder.execute();
        Assert.assertFalse(receiver.orderFailed);
        assertTotalEquals(10.0);
    }

    private void assertTotalEquals(Double expectedTotal) {
        Assert.assertEquals(expectedTotal, createPurchaseOrder.getTotal(), 0.01);
    }

    private Date givenDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

    private ProductInfo givenProductInfo(String name, String description, int unitsInStock, double unitPrice) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.name = name;
        productInfo.description = description;
        productInfo.unitsInStock = unitsInStock;
        productInfo.price = unitPrice;
        productInfo.id = productRepository.createProductInfoID();
        RegisterProductUseCase registerProductUseCase = new RegisterProductUseCase(new FakeProductReceiver(), productInfo, productRepository);
        registerProductUseCase.execute();
        return productInfo;
    }

}
