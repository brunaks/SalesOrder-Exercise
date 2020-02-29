package Persistence;

import Entities.Product.ProductInfo;
import Interfaces.Persistence.ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class JDBCProductRepository implements ProductRepository {
    private final Connection connection;

    public JDBCProductRepository() {
        connection = DatabaseConnectionFactory.getConnection();
    }

    public void saveProduct(ProductInfo product) {
        if (getProductInfoById(product.id) == null)
            insert(product);
        else
            update(product);
    }

    private void update(ProductInfo product) {
        String sql = "BEGIN update_product (?, ?, ?, ?, ?); end;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, product.id);
            stmt.setString(2, product.name);
            stmt.setString(3, product.description);
            stmt.setDouble(4, product.price);
            stmt.setInt(5, product.unitsInStock);

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insert(ProductInfo product) {
        String sql = "insert into product " +
                "(product_id, product_name, description, CAST ( price AS MONEY ), units_in_stock)" +
                " values (?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, product.id);
            stmt.setString(2, product.name);
            stmt.setString(3, product.description);
            //stmt.setDouble(4, product.price);
            stmt.setString(4, "$" + product.price);
            stmt.setInt(5, product.unitsInStock);

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ProductInfo getProductInfoByName(String productName) {
        String sql = "select * from product where product_name = ?";
        ResultSet result;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productName);
            result = stmt.executeQuery();
            if (result.next() == false) {
                return null;
            } else {
                return buildProductInfo(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ProductInfo buildProductInfo(ResultSet result) {
        ProductInfo info = new ProductInfo();
        try {
            do {
                info = new ProductInfo();
                info.id = result.getString("product_id");
                info.name = result.getString("product_name");
                info.description = result.getString("description");
                info.price = result.getDouble("price");
                info.unitsInStock = result.getInt("units_in_stock");
            } while (result.next());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return info;
    }

    public ArrayList<ProductInfo> getAllProductsInfoSaved() {
        String sql = "select * from product";
        ResultSet result;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            result = stmt.executeQuery();
            return buildProductInfos(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<ProductInfo> buildProductInfos(ResultSet result) {
        ArrayList<ProductInfo> infos = new ArrayList<>();
        try {
            while (result.next()) {
                ProductInfo info = new ProductInfo();
                info.id = result.getString("product_id");
                info.name = result.getString("product_name");
                info.description = result.getString("description");
                info.price = result.getDouble("price");
                info.unitsInStock = result.getInt("units_in_stock");
                infos.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infos;
    }

    public ProductInfo getProductInfoById(String id) {
        String sql = "select * from product where product_id = ?";
        ResultSet result;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            result = stmt.executeQuery();
            if (result.next() == false) {
                return null;
            } else {
                return buildProductInfo(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProduct(String productId, ProductInfo newProductInfo) {
        this.update(newProductInfo);
    }

    public void deleteProductWithId(String productId) {
        String sql = "delete from product where product_id = ?";
        ResultSet result;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, productId);
            result = stmt.executeQuery();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String createProductInfoID() {
        return UUID.randomUUID().toString();
    }
}
