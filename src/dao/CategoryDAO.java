package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.ConnectionDatabase;
import model.Category;

public class CategoryDAO {
    private Connection connection;

    public CategoryDAO() throws SQLException {
        connection = new ConnectionDatabase().getConnection();
    }

    public void add(Category category) {
        String sql = "INSERT INTO category(name) VALUES(?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.execute();
            System.out.println("Categoria adicionada!");
        } catch (SQLException u) {
            System.out.println("Erro ao adicionar categoria: " + u.getMessage());
            throw new RuntimeException(u);
        }
    }

    public Category getCategory(Category categoryId) {
        String sql = "SELECT * FROM category WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId.getId());
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                Category category = new Category();
                category.setId(result.getInt("id"));
                category.setName(result.getString("name"));
                return category;
            } else {
                System.out.println("ID inválido.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar categoria: " + e.getMessage());
            return null;
        }
    }

    public void update(Category category) {
        String sql = "UPDATE category SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Categoria atualizada!");
            } else {
                System.out.println("Nenhuma categoria encontrada para atualizar.");
            }
        } catch (SQLException u) {
            System.out.println("Erro ao atualizar categoria: " + u.getMessage());
            throw new RuntimeException(u);
        }
    }

    public void delete(Category category) {
        String sql = "DELETE FROM category WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, category.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Categoria excluída!");
            } else {
                System.out.println("ID inválido.");
            }
        } catch (SQLException u) {
            System.out.println("Erro ao excluir categoria: " + u.getMessage());
            throw new RuntimeException(u);
        }
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> data = new ArrayList<>();
        String sql = "SELECT * FROM category";
        try (PreparedStatement ps = this.connection.prepareStatement(sql);
                ResultSet result = ps.executeQuery()) {

            while (result.next()) {
                data.add(new Category(
                        result.getInt("id"),
                        result.getString("name")));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao pegar a lista: " + e.getMessage());
        }
        return data;
    }

    public void testConnection() {
        try (Connection objConnection = new ConnectionDatabase().getConnection()) {
            System.out.println("Conexão realizada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao testar conexão: " + e.getMessage());
        }
    }
}