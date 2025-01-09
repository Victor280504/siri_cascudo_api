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

    public void add(UserRole UserRole) {
        try {
            String sql;

            sql = "INSERT INTO user_roles(userId, role) VALUES(?)";
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setInt(1, UserRole.getIdUser());
            stmt.setString(2, UserRole.getRole());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Regra de usuário adicionada!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public UserRole getUserRole(UserRole userRoleId) {
        try {
            String sql = "";

            sql = "SELECT * FROM user_role WHERE idUser = " + userRoleId.getIdUser() + "AND role = "
                    + userRoleId.getRole();

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            UserRole userRole = new UserRole();

            while (result.next()) {
                userRole.setIdUser(result.getInt("id_user"));
                userRole.setRole(result.getString("role"));

            }

            ps.close();
            result.close();

            if (userRole.getIdUser() == 0) {
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
            this.connection.close();

            return data;
        } catch (SQLException e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Erro ao pegar a lista.");
            return null;
        }
    }

    public void update(UserRole userRole) {
        try {
            String sql;
            UserRole oldUserRole = this.getUserRole(userRole);

            sql = "UPDATE user_role SET idUser, role = ? WHERE idUser = " + userRole.getIdUser() + "AND role = "
                    + userRole.getRole();
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setInt(1, Integer.valueOf(userRole.getIdUser()) == 0 ? oldUserRole.getIdUser()
                    : userRole.getIdUser());
            stmt.setString(1, String.valueOf(userRole.getRole()).isEmpty() ? oldUserRole.getRole()
                    : userRole.getRole());

            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Regra de usuário atuaizada!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(UserRole userRole) {
        try {
            if (userRole == null) {
                JOptionPane.showMessageDialog(null, "Erro: Regra de usuário inválida.");
                return;
            }

            UserRole existingUserRole = this.getUserRole(userRole);

            if (existingUserRole != null && existingUserRole.getIdUser() > 0 && existingUserRole.getRole() != "") {
                String sql = "DELETE FROM user_roles WHERE idUser = ? AND role = ?";
                try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
                    stmt.setInt(1, userRole.getIdUser());
                    stmt.setString(2, userRole.getRole());
                    stmt.execute();
                }
                JOptionPane.showMessageDialog(null, "Regra de usuário excluída com sucesso!");
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
