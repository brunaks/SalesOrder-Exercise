import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by I848075 on 20/08/2015.
 */
public interface ProductRepository {
    void saveProduct(Product product);
    Product getProductByName(String productName);
    Collection<Product> getAllProductsSaved();
    Product getProductById(String id);
}
