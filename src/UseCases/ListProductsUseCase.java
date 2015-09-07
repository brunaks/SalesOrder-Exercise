package UseCases;

import Entities.Product;
import Entities.ProductInfo;
import Interfaces.ProductRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by i848075 on 28/08/2015.
 */
public class ListProductsUseCase {

    ProductRepository repository;

    public ListProductsUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    public ArrayList<ProductInfo> returnsAllProducts() {
        Collection<Product> products = this.repository.getAllProductsSaved();
        ArrayList<ProductInfo> productsInfo = new ArrayList<ProductInfo>();
        for (Product p: products) {
            ProductInfo productInfo = new ProductInfo();
            productInfo.id = p.getId();
            productInfo.name = p.getName();
            productInfo.description = p.getDescription();
            productInfo.price = p.getPrice();
            productInfo.unitsInStock = p.getUnitsInStock();
            productsInfo.add(productInfo);
        }
        return productsInfo;
    }
}
