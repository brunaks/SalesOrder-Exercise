package TestDoubles.Persistence;

import Entities.Product.Product;
import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by i848075 on 01/09/2015.
 */
public class FileProductRepository implements ProductRepository {

    ArrayList<Product> productsSaved = new ArrayList<Product>();

    public FileProductRepository() {
        loadAll();
    }

    private void loadAll() {

        this.productsSaved.clear();
        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream("products.csv"));
            ObjectInputStream inputStream = new ObjectInputStream(stream);
            while (true) {
                try {
                    productsSaved.add((Product) inputStream.readObject());
                } catch (EOFException e) {
                    break;
                }
            }
            inputStream.close();
        } catch (Exception e) {

        }
    }

    public void saveProduct(ProductInfo productInfo) {

        productInfo.id = createProductInfoID();
        Product productToSave = createProductToSave(productInfo);
        this.productsSaved.add(productToSave);
        updateFile();
    }

    private void updateFile() {
        File file = new File("products.csv");
        file.delete();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file, false));
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);

            for (int i = 0; i < productsSaved.size(); i++) {
                objectStream.writeObject(productsSaved.get(i));
            }
            objectStream.close();

        } catch (Exception e) {

        }
    }

    private Product createProductToSave(ProductInfo productInfo) {
        Product productToSave = new Product();
        productToSave.setId(productInfo.id);
        productToSave.setName(productInfo.name);
        productToSave.setDescription(productInfo.description);
        productToSave.setPrice(productInfo.price);
        productToSave.setUnitsInStock(productInfo.unitsInStock);
        return productToSave;
    }

    @Override
    public ProductInfo getProductInfoByName(String productName) {
        for (int i = 0; i < this.productsSaved.size(); i++) {
            if (this.productsSaved.get(i).getName().equalsIgnoreCase(productName)) {
                return createProductInfo(this.productsSaved.get(i));
            }
        }
        return null;
    }

    private ProductInfo createProductInfo(Product product) {
        ProductInfo info = new ProductInfo();
        info.id = product.getId();
        info.name = product.getName();
        info.description = product.getDescription();
        info.price = product.getPrice();
        info.unitsInStock = product.getUnitsInStock();
        return info;
    }

    @Override
    public ArrayList<ProductInfo> getAllProductsInfoSaved() {
        return createProductsInfo();
    }

    private ArrayList<ProductInfo> createProductsInfo() {
        ArrayList<ProductInfo> productsInfo = new ArrayList<ProductInfo>();
        for (int i = 0; i < this.productsSaved.size(); i++) {
            productsInfo.add(createProductInfo(productsSaved.get(i)));
        }
        return productsInfo;
    }

    @Override
    public ProductInfo getProductInfoById(String id) {
        for (int i = 0; i < this.productsSaved.size(); i++) {
            if (this.productsSaved.get(i).getId().equals(id)) {
                return createProductInfo(this.productsSaved.get(i));
            }
        }
        return null;
    }

    private Product getProductById(String id) {
        for (int i = 0; i < this.productsSaved.size(); i++) {
            if (this.productsSaved.get(i).getId().equals(id)) {
                return this.productsSaved.get(i);
            }
        }
        return null;
    }

    @Override
    public void updateProduct(String productId, ProductInfo newProductInfo) {
        Product product = getProductById(productId);
        if (product != null) {
            product.setName(newProductInfo.name);
            product.setDescription(newProductInfo.description);
            product.setPrice(newProductInfo.price);
            product.setUnitsInStock(newProductInfo.unitsInStock);
        }
        updateFile();
    }

    @Override
    public void deleteProductWithId(String productId) {
        Product productToDelete = getProductById(productId);
        if (productToDelete != null) {
            productsSaved.remove(productToDelete);
            updateFile();
        }
    }

    @Override
    public String createProductInfoID() {
        return UUID.randomUUID().toString();
    }
}
