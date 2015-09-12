package Interfaces.Persistence;

import Entities.Product.Product;
import Entities.Product.ProductInfo;

import java.util.Collection;

/**
 * Created by I848075 on 20/08/2015.
 */
public interface ProductRepository {
    void saveProduct(ProductInfo product);
    ProductInfo getProductInfoByName(String productName);
    Collection<Product> getAllProductsSaved();
    ProductInfo getProductInfoById(String id);
    void updateProduct(String productId, ProductInfo newProductInfo);
    void deleteProductWithId(String productId);
}
