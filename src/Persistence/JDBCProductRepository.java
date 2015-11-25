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
        connection = ConnectionFactory.getConnection();
    }

    public void saveProduct(ProductInfo product) {
        if (hasWithId(product.id))
            insert(product);
        else
            update(product);
    }

    private boolean hasWithId(String id) {
        return false;
    }

    private void update(ProductInfo product) {
        String sql = "update product set " +
                "name = ?, description = ?, price = ?, units_in_stock = ?" +
                "where id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, product.name);
            stmt.setString(2, product.description);
            stmt.setDouble(3, product.price);
            stmt.setInt(4, product.unitsInStock);
            stmt.setString(5, product.id);

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insert(ProductInfo product) {
        String sql = "insert into product " +
                "(id, name,description,price,units_in_stock)" +
                " values (?,?,?,?,?)";
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

    public ProductInfo getProductInfoByName(String productName) {
        String sql = "select single * from product where name = ?;";
        ResultSet result;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, productName);

            result = stmt.executeQuery();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return buildProductInfo(result);
    }

    private ProductInfo buildProductInfo(ResultSet result) {
        ProductInfo info = new ProductInfo();
        try {
            while (result.next()) {

                info = new ProductInfo();
                info.id = result.getString("id");
                info.name = result.getString("name");
                info.description = result.getString("description");
                info.price = result.getDouble("price");
                info.unitsInStock = result.getInt("units_in_stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList<ProductInfo> getAllProductsInfoSaved() {
        String sql = "select * from product";
        ResultSet result;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            result = stmt.executeQuery();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return buildProductInfos(result);
    }

    private ArrayList<ProductInfo> buildProductInfos(ResultSet result) {
        ArrayList<ProductInfo> infos = new ArrayList<>();
        try {
            while (result.next()) {
                ProductInfo info = new ProductInfo();
                info.id = result.getString("id");
                info.name = result.getString("name");
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
        String sql = "select single * from product where id = ?;";
        ResultSet result;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, id);

            result = stmt.executeQuery();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return buildProductInfo(result);
    }

    public void updateProduct(String productId, ProductInfo newProductInfo) {
        this.update(newProductInfo);
    }

    public void deleteProductWithId(String productId) {
        String sql = "delete from product where id = ?";
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
