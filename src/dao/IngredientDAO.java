package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import database.ConnectionDatabase;
import model.Ingredient;

public class IngredientDAO {
    private Connection connection;

    public IngredientDAO() throws SQLException {
        connection = new ConnectionDatabase().getConnection();
    }

    public void add(Ingredient Ingredient) {
        try {
            String sql;

            sql = "INSERT INTO ingredient(description, quantity, price) VALUES(?, ?, ?)";
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setString(1, Ingredient.getDescription());
            stmt.setInt(2, Ingredient.getQuantity());

            stmt.setDouble(3, Ingredient.getPrice());
            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Ingrediente adicionado!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public Ingredient getIngredient(Ingredient ingredientId) {
        try {
            String sql = "";

            sql = "SELECT * FROM ingredient WHERE id = " + ingredientId.getId();

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            Ingredient ingredient = new Ingredient();

            while (result.next()) {
                ingredient.setId(result.getInt("id"));
                ingredient.setDescription(result.getString("description"));
                ingredient.setQuantity(result.getInt("quantity"));
                ingredient.setPrice(result.getDouble("price"));
            }

            ps.close();
            result.close();

            if (ingredient.getId() == 0) {
                JOptionPane.showMessageDialog(null, "ID inválido.");
            }

            return ingredient;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar ingrediente");
            return null;
        }

    }

    public ArrayList<Ingredient> getAllIngredients() {
        try {

            ArrayList<Ingredient> data = new ArrayList<Ingredient>();

            PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM ingredient");
            ResultSet result = ps.executeQuery();

            while (result.next()) {

                data.add(new Ingredient(
                        result.getInt("id"),
                        result.getString("description"), result.getInt("quantity"),
                        result.getDouble("price")));
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

    public void update(Ingredient ingredient) {
        try {
            Ingredient oldIngredient = this.getIngredient(ingredient);

            String sql = "UPDATE ingredient SET description = ?, quantity = ?, price = ? WHERE id = " + ingredient.getId();
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            String description = ingredient.getDescription().isEmpty() ? oldIngredient.getDescription()
                    : ingredient.getDescription();
            stmt.setString(1, description);

            int quantity = (ingredient.getQuantity() == 0) ? oldIngredient.getQuantity() : ingredient.getQuantity();
            stmt.setInt(2, quantity);

            double price = (ingredient.getPrice() == 0.0) ? oldIngredient.getPrice() : ingredient.getPrice();
            stmt.setDouble(3, price);

            stmt.executeUpdate();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Ingrediente atualizado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Ingredient ingredient) {
        try {
            if (ingredient == null) {
                JOptionPane.showMessageDialog(null, "Erro: Ingrediente inválido.");
                return;
            }

            Ingredient existingIngredient = this.getIngredient(ingredient);

            if (existingIngredient != null && existingIngredient.getId() > 0) {
                String sql = "DELETE FROM ingredient WHERE id = ?";
                try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
                    stmt.setInt(1, ingredient.getId());
                    stmt.execute();
                }
                JOptionPane.showMessageDialog(null, "Ingrediente excluído com sucesso!");
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
