package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import database.ConnectionDatabase;
import model.UserRole;

public class UserRoleDAO {
    private Connection connection;

    public UserRoleDAO() throws SQLException {
        connection = new ConnectionDatabase().getConnection();
    }

    public void add(UserRole userRole) {
        try {
            String sql = "INSERT INTO user_roles(id_user, role) VALUES(?, ?)";
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setInt(1, userRole.getIdUser());
            stmt.setString(2, userRole.getRole());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Regra de usuário adicionada!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public UserRole getRoleByUser(UserRole userId) {
        try {
            String sql = "SELECT * FROM user_roles WHERE id_user = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId.getIdUser());
            ResultSet result = ps.executeQuery();

            UserRole userRole = null;

            if (result.next()) {
                userRole = new UserRole();
                userRole.setIdUser(result.getInt("id_user"));
                userRole.setRole(result.getString("role"));
            }

            ps.close();
            result.close();

            if (userRole == null) {
                JOptionPane.showMessageDialog(null, "ID inválido.");
            }

            return userRole;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar regra de usuário");
            return null;
        }
    }

    public ArrayList<UserRole> getAllCategories() {
        try {
            ArrayList<UserRole> data = new ArrayList<UserRole>();

            PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM user_roles");
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                data.add(new UserRole(
                        result.getInt("id_user"),
                        result.getString("role")));
            }
            ps.close();
            result.close();

            return data;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar a lista.");
            return null;
        }
    }

    public void update(UserRole userRole) {
        try {
            String sql;
            UserRole oldUserRole = this.getRoleByUser(userRole);

            sql = "UPDATE user_roles SET id_user = ?, role = ? WHERE id_user = ?";
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setInt(1, userRole.getIdUser() == 0 ? oldUserRole.getIdUser() : userRole.getIdUser());
            stmt.setString(2, userRole.getRole().isEmpty() ? oldUserRole.getRole() : userRole.getRole());
            stmt.setInt(3, userRole.getIdUser());

            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Regra de usuário atualizada!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(UserRole userRole) {
        try {
            if (userRole == null || userRole.getIdUser() <= 0 || userRole.getRole().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Erro: Regra de usuário inválida.");
                return;
            }

            String sql = "DELETE FROM user_roles WHERE id_user = ? AND role = ?";
            try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
                stmt.setInt(1, userRole.getIdUser());
                stmt.setString(2, userRole.getRole());
                stmt.execute();
            }
            JOptionPane.showMessageDialog(null, "Regra de usuário excluída com sucesso!");
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
