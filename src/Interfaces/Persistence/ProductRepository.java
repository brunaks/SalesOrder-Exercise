package Interfaces.Persistence;

import Entities.Product.Product;
import Entities.Product.ProductInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by I848075 on 20/08/2015.
 */
public interface ProductRepository {
    void saveProduct(ProductInfo product);
    ProductInfo getProductInfoByName(String productName);
    ArrayList<ProductInfo> getAllProductsInfoSaved();
    ProductInfo getProductInfoById(String id);
    void updateProduct(String productId, ProductInfo newProductInfo);
    void deleteProductWithId(String productId);
    void createProductInfoID(ProductInfo productInfo);
}
