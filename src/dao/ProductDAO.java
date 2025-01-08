package dao;

import database.ConnectionDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Product;

public class ProductDAO {

    private Connection connection;

    public ProductDAO() throws SQLException {
        connection = new ConnectionDatabase().getConnection();
    }

    public void testConnection() throws SQLException {
        try (Connection objConnection = new ConnectionDatabase().getConnection()) {
            JOptionPane.showMessageDialog(null, "Conexão realizada com sucesso!");
        }
    }

    public void add(Product product) {
        try {
            String sql;

            sql = "INSERT INTO product(description, image, quantity, price, id_category) VALUES(?)";
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setString(1, product.getDescription());
            stmt.setString(2, product.getImage());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getIdCategory());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Produto adicionado!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public Product getProduct(Product productId) {
        try {
            String sql;

            sql = "SELECT * FROM product WHERE id = " + productId.getId();

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            Product product = new Product();

            while (result.next()) {
                product.setId(result.getInt("id"));
                product.setDescription(result.getString("description"));
                product.setImage(result.getString("image"));
                product.setQuantity(result.getInt("quantity"));
                product.setPrice(result.getDouble("price"));
                product.setIdCategory(result.getInt("id_category"));
            }

            ps.close();
            result.close();

            if (product.getId() == 0) {
                JOptionPane.showMessageDialog(null, "ID inválido");
            }

            return product;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar produto");
            return null;
        }
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> data = new ArrayList<Product>();

        try {
            PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM product");
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                data.add(new Product(
                        result.getInt("id"),
                        result.getString("description"),
                        result.getString("image"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getInt("id_category")));
            }
            ps.close();
            result.close();
            this.connection.close();

        } catch (Exception e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Erro ao pegar a lista de produtos");
        }
        return data;
    }

    public void update(Product product) {
        try {
            String sql;

            Product oldProduct = this.getProduct(product);

            sql = "UPDATE product SET description, image, quantitiy, price, id_category = ? WHERE id = " + product.getId();
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setString(1, String.valueOf(product.getDescription()).isEmpty() ? oldProduct.getDescription()
                    : product.getDescription());
            stmt.setString(2, String.valueOf(product.getImage()).isEmpty() ? oldProduct.getImage()
                    : product.getImage());
            stmt.setInt(3, product.getQuantity() == 0 ? oldProduct.getQuantity()
                    : product.getQuantity());
            stmt.setDouble(4, product.getPrice() == 0.0 ? oldProduct.getPrice()
                    : product.getPrice());
            stmt.setInt(5, product.getIdCategory() == 0 ? oldProduct.getIdCategory()
                    : product.getIdCategory());

            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Produto atuaizado!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(Product product) {
        try {
            if (product == null) {
                JOptionPane.showMessageDialog(null, "Erro: Produto inválido.");
                return;
            }
    
            Product existingProduct = this.getProduct(product);

            if (existingProduct != null && existingProduct.getId() > 0) {
                String sql = "DELETE FROM product WHERE id = ?";
                try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
                    stmt.setInt(1, product.getId());
                    stmt.execute();
                }
                JOptionPane.showMessageDialog(null, "Produto excluído com sucesso!");
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
}
