package Entities.Order;

import Entities.Product.ProductInfo;

/**
 * Created by Bruna Koch Schmitt on 17/11/2015.
 */
public class OrderItem {

    public ProductInfo productInfo;
    public int quantity;

    public OrderItem(ProductInfo productInfo, int quantity) {
        this.productInfo = productInfo;
        this.quantity = quantity;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public double getQuantity() {
        return quantity;
    }
}
