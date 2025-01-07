package dao;

import database.ConnectionDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.SaleProduct;

public class SaleProductDAO {
    private Connection connection;

    public SaleProductDAO() throws SQLException {
        connection = new ConnectionDatabase().getConnection();
    }

    public void testConnection() throws SQLException {
        try (Connection objConnection = new ConnectionDatabase().getConnection()) {
            JOptionPane.showMessageDialog(null, "Conexão realizada com sucesso!");
        }
    }

    public void add(SaleProduct saleProduct) {
        try {
            String sql;

            sql = "INSERT INTO sale_product(idUser, idProduct, quantity, value) VALUES(?)";
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setInt(1, saleProduct.getIdUser());
            stmt.setInt(2, saleProduct.getIdProduct());
            stmt.setInt(3, saleProduct.getQuantity());
            stmt.setDouble(4, saleProduct.getValue());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Venda_Produto adicionado!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
}
