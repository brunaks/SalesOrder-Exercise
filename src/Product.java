import java.io.Serializable;

/**
 * Created by I848075 on 19/08/2015.
 */
public class Product implements Serializable{
    private String name;
    private String description;
    private double price;
    private int unitsInStock;
    private String id;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUnitsInStock(int unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getUnitsInStock() {
        return unitsInStock;
    }
}
