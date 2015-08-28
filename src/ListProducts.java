import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by i848075 on 28/08/2015.
 */
public class ListProducts {

    ProductRepository repository;

    public ListProducts(ProductRepository repository) {
        this.repository = repository;
    }

    public ArrayList<ProductInfo> returnsAllProducts() {
        Collection<Product> products = this.repository.getAllProductsSaved();
        ArrayList<ProductInfo> productsInfo = new ArrayList<ProductInfo>();
        for (Product p: products) {
            ProductInfo productInfo = new ProductInfo();
            productInfo.name = p.getName();
            productInfo.description = p.getDescription();
            productInfo.price = p.getPrice();
            productInfo.unitsInStock = p.getUnitsInStock();
            productsInfo.add(productInfo);
        }
        return productsInfo;
    }
}
