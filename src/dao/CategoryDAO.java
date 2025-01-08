package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import database.ConnectionDatabase;
import model.Category;

public class CategoryDAO {
    private Connection connection;

    public CategoryDAO() throws SQLException {
        connection = new ConnectionDatabase().getConnection();
    }

    public void add(Category category) {
        try {
            String sql;

            sql = "INSERT INTO category(name) VALUES(?)";
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setString(1, category.getName());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Categoria adicionada!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public Category getCategory(Category categoryId) {
        try {
            String sql = "";

            sql = "SELECT * FROM category WHERE id = " + categoryId.getId();

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            Category category = new Category();

            while (result.next()) {
                category.setId(result.getInt("id"));
                category.setName(result.getString("name"));

            }

            ps.close();
            result.close();

            if (category.getId() == 0) {
                JOptionPane.showMessageDialog(null, "ID inválido.");
            }

            return category;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar categoria");
            return null;
        }

    }

    public ArrayList<Category> getAllCategories() {
        try {

            ArrayList<Category> data = new ArrayList<Category>();

            PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM category");
            ResultSet result = ps.executeQuery();

            while (result.next()) {

                data.add(new Category(
                        result.getInt("id"),
                        result.getString("name")));

            }
            ps.close();
            result.close();
            this.connection.close();

            return data;
        } catch (SQLException e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Erro ao pegar a lista.");
            return null;
        }
    }

    public void update(Category category) {
        try {
            String sql;
            Category oldCategory = this.getCategory(category);

            sql = "UPDATE category SET name = ? WHERE id = " + category.getId();
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setString(1, String.valueOf(category.getName()).isEmpty() ? oldCategory.getName()
                    : category.getName());

            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Categoria atuaizada!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(Category category) {
        try {
            if (category == null) {
                JOptionPane.showMessageDialog(null, "Erro: Categoria inválida.");
                return;
            }
    
            Category existingCategory = this.getCategory(category);

            if (existingCategory != null && existingCategory.getId() > 0) {
                String sql = "DELETE FROM category WHERE id = ?";
                try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
                    stmt.setInt(1, category.getId());
                    stmt.execute();
                }
                JOptionPane.showMessageDialog(null, "Categoria excluída com sucesso!");
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void testConnection() throws SQLException {
        try (Connection objConnection = new ConnectionDatabase().getConnection()) {
            JOptionPane.showMessageDialog(null, "Conexão realizada com sucesso!");
        }
    }

}
