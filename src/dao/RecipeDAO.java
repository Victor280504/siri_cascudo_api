package dao;

import database.ConnectionDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Recipe;

public class RecipeDAO {

    private Connection connection;

    public RecipeDAO() throws SQLException {
        connection = new ConnectionDatabase().getConnection();
    }

    public void testConnection() throws SQLException {
        try (Connection objConnection = new ConnectionDatabase().getConnection()) {
            JOptionPane.showMessageDialog(null, "Conexão realizada com sucesso!");
        }
    }

    public void add(Recipe recipe) {
        try {
            String sql;

            sql = "INSERT INTO revenue(id_ingredient, id_product, quantity) VALUES(?)";
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setInt(1, recipe.getIdIngredient());
            stmt.setInt(2, recipe.getIdProduct());
            stmt.setInt(3, recipe.getQuantity());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Receita adicionada!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

        public Recipe getRecipe(Recipe recipeId) {
        try {
            String sql;

            sql = "SELECT * FROM revenue WHERE id_ingredient = " + recipeId.getIdIngredient() + "AND id_product =" +  recipeId.getIdProduct();

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            Recipe recipe = new Recipe();

            while (result.next()) {
                recipe.setIdIngredient(result.getInt("id_ingredient"));
                recipe.setIdProduct(result.getInt("id_product"));
                recipe.setQuantity(result.getInt("quantity"));
            }

            ps.close();
            result.close();

            if (recipe.getIdIngredient() == 0 && recipe.getIdProduct() == 0 ) {
                JOptionPane.showMessageDialog(null, "IDs inválidos");
            }

            return recipe;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar receita");
            return null;
        }
    }

    public ArrayList<Recipe> getAllSaleRecipes() {
        ArrayList<Recipe> data = new ArrayList<Recipe>();

            try {
            PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM revenue");
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                data.add(new Recipe(
                        result.getInt("id_ingredient"),
                        result.getInt("id_product"),
                        result.getInt("quantity")));
            }
            ps.close();
            result.close();
            this.connection.close();

        } catch (Exception e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Erro ao pegar a lista de receitas");
        }
        return data;
    }

    public void update(Recipe recipe) {
        try {
            String sql;

            Recipe oldRecipe = this.getRecipe(recipe);

            sql = "UPDATE revenue SET id_ingredient, id_product, quantity = ? WHERE id_ingredient = " + recipe.getIdIngredient() + "AND id_product =" +  recipe.getIdProduct();
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setInt(1, recipe.getIdIngredient() == 0 ? oldRecipe.getIdIngredient()
                    : recipe.getIdIngredient());
            stmt.setInt(2, recipe.getIdProduct() == 0 ? oldRecipe.getIdProduct()
                    : recipe.getIdProduct());
            stmt.setInt(3, recipe.getQuantity() == 0 ? oldRecipe.getQuantity()
                    : recipe.getQuantity());  

            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Receita atuaizada!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(Recipe recipe) {
        try {
            if (recipe == null) {
                JOptionPane.showMessageDialog(null, "Erro: receita inválida.");
                return;
            }
    
            Recipe existingRecipe = this.getRecipe(recipe);

            if (existingRecipe != null && existingRecipe.getIdIngredient() > 0 && existingRecipe.getIdProduct() > 0) {
                String sql = "DELETE FROM revenue WHERE id_ingredient = ? AND id_product = ?";
                try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
                    stmt.setInt(1, recipe.getIdIngredient());
                    stmt.setInt(2, recipe.getIdProduct());
                    stmt.execute();
                }
                JOptionPane.showMessageDialog(null, "Receita excluída com sucesso!");
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
}
