package dao;

import database.ConnectionDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Sale;
import model.SaleProduct;

public class SaleDAO {
    
    //instanciando objeto connection para banco de dados
    private Connection connection;

    //CONSTRUTOR
    //estabelece conexão com banco de dados
    public SaleDAO() throws SQLException {
        connection = new ConnectionDatabase().getConnection();
    }

    public void testConnection() throws SQLException {
        try (Connection objConnection = new ConnectionDatabase().getConnection()) {
            JOptionPane.showMessageDialog(null, "Conexão realizada com sucesso!");
        }
    }

    public void add(Sale sale){
        String sql;
        sql = "INSERT INTO sale(date, paymentMethod, idUser) VALUES(?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setDate(1, sale.getDate());
            stmt.setString(2, sale.getPaymentMethod());
            stmt.setInt(3, sale.getIdUser());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Venda adicionada!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public Sale getSale(Sale saleId) {
        try {
            String sql;

            sql = "SELECT * FROM sale WHERE id = " + saleId.getId();

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            Sale sale = new Sale();

            while (result.next()) {
                sale.setId(result.getInt("id"));
                sale.setDate(result.getDate("date"));
                sale.setPaymentMethod(result.getString("paymentMethod"));
                sale.setIdUser(result.getInt("idUser"));
            }

            ps.close();
            result.close();

            if (sale.getId() == 0) {
                JOptionPane.showMessageDialog(null, "ID inválido.");
            }

            return sale;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar venda.");
            return null;
        }
    }

    public ArrayList<Sale> getAllSales() {
        ArrayList<Sale> data = new ArrayList<Sale>();

            try {
            PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM sale");
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                data.add(new Sale(
                    result.getInt("id"),
                    result.getDate("date"),
                    result.getString("paymentMethod"),
                    result.getInt("idUser")
                ));
            }
            ps.close();
            result.close();
            this.connection.close();

        } catch (Exception e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de vendas");
        }
        return data;
    }

    public void update(Sale sale){
        try {
            String sql;

            Sale oldSale = this.getSale(sale);

            sql = "UPDATE sale SET date, paymentMethod, idUser = ? WHERE id = " + sale.getId();
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setDate(1, (sale.getDate() == null) ? oldSale.getDate()
                    : sale.getDate());
            stmt.setString (2, String.valueOf(sale.getPaymentMethod()).isEmpty() ? oldSale.getPaymentMethod():
                    sale.getPaymentMethod());
            stmt.setInt(3, sale.getIdUser() == 0 ? oldSale.getIdUser()
                    : sale.getIdUser());

            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Venda atuaizada!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(Sale sale) {
        try {
            if (sale == null) {
                JOptionPane.showMessageDialog(null, "Erro: Venda inválida.");
                return;
            }
    
            Sale existingSale = this.getSale(sale);

            if (existingSale != null && existingSale.getId() > 0) {
                String sql = "DELETE FROM sale WHERE id = ?";
                try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
                    stmt.setInt(1, sale.getId());
                    stmt.execute();
                }
                JOptionPane.showMessageDialog(null, "Venda excluída com sucesso!");
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
}
