package UseCases.Product;

import Entities.Product.Product;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;

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
        ArrayList<ProductInfo> productsInfo = this.repository.getAllProductsInfoSaved();
        return productsInfo;
    }
}
