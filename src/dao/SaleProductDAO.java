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

            sql = "INSERT INTO sale_product(id_user, id_product, quantity, value) VALUES(?, ?, ?, ?)";
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

    public SaleProduct getSaleProduct(SaleProduct saleProductId) {
        try {
            String sql;

            sql = "SELECT * FROM sale_product WHERE id_user = " + saleProductId.getIdUser() + "AND id_product =" +  saleProductId.getIdProduct();

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            SaleProduct saleProduct = new SaleProduct();

            while (result.next()) {
                saleProduct.setIdUser(result.getInt("id_user"));
                saleProduct.setIdProduct(result.getInt("id_product"));
                saleProduct.setQuantity(result.getInt("quantity"));
                saleProduct.setValue(result.getDouble("value"));
            }

            ps.close();
            result.close();

            if (saleProduct.getIdUser() == 0 && saleProduct.getIdProduct() == 0 ) {
                JOptionPane.showMessageDialog(null, "IDs inválidos");
            }

            return saleProduct;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar venda_Produto");
            return null;
        }
    }

    public ArrayList<SaleProduct> getAllSaleProducts() {
        ArrayList<SaleProduct> data = new ArrayList<SaleProduct>();

            try {
            PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM sale_product");
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                data.add(new SaleProduct(
                        result.getInt("id_user"),
                        result.getInt("id_product"),
                        result.getInt("quantity"),
                        result.getDouble("value")));
            }
            ps.close();
            result.close();
            this.connection.close();

        } catch (Exception e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Erro ao pegar a lista de venda_Produtos");
        }
        return data;
    }

    public void update(SaleProduct saleProduct) {
        try {
            String sql;

            SaleProduct oldSaleProduct = this.getSaleProduct(saleProduct);

            sql = "UPDATE sale_product SET id_user = ?, id_product = ?, quantity = ?, value = ? WHERE id_user = " + saleProduct.getIdUser() + "AND id_product =" +  saleProduct.getIdProduct();
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setInt(1, saleProduct.getIdUser() == 0 ? oldSaleProduct.getIdUser()
                    : saleProduct.getIdUser());
            stmt.setInt(2, saleProduct.getIdProduct() == 0 ? oldSaleProduct.getIdProduct()
                    : saleProduct.getIdProduct());
            stmt.setInt(3, saleProduct.getQuantity() == 0 ? oldSaleProduct.getQuantity()
                    : saleProduct.getQuantity());
            stmt.setDouble(4, saleProduct.getValue() == 0.0 ? oldSaleProduct.getValue()
                    : saleProduct.getValue());   

            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(null, "venda_Produto atuaizado!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(SaleProduct saleProduct) {
        try {
            if (saleProduct == null) {
                JOptionPane.showMessageDialog(null, "Erro: venda_Produto inválido.");
                return;
            }
    
            SaleProduct existingSaleProduct = this.getSaleProduct(saleProduct);

            if (existingSaleProduct != null && existingSaleProduct.getIdUser() > 0 && existingSaleProduct.getIdProduct() > 0) {
                String sql = "DELETE FROM sale_product WHERE id_user = ? AND id_product = ?";
                try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
                    stmt.setInt(1, saleProduct.getIdUser());
                    stmt.setInt(2, saleProduct.getIdProduct());
                    stmt.execute();
                }
                JOptionPane.showMessageDialog(null, "venda_Produto excluído com sucesso!");
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
}
