/**
 * Created by I848075 on 19/08/2015.
 */
public class ProductInfo {
    public String id;
    public String name;
    public String description;
    public double price;
    public int    unitsInStock;

    public boolean isValid() {
        if (this.nameIsValid() && this.descriptionIsValid() && this.priceIsValid() && this.unitsInStockIsValid())
            return true;
        else
            return false;
    }

    private boolean unitsInStockIsValid() {
        return this.unitsInStock >= 0;
    }

    private boolean priceIsValid() {
        return this.price > 0;
    }

    private boolean descriptionIsValid() {
        return !this.description.isEmpty();
    }

    private boolean nameIsValid() {
        return !this.name.isEmpty();
    }
}
