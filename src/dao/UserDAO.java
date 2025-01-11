package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import database.ConnectionDatabase;
import model.User;

public class UserDAO {
    private Connection connection;

    public UserDAO() throws SQLException {
        connection = new ConnectionDatabase().getConnection();
    }

    public void add(User user) {
        try {
            String sql;

            sql = "INSERT INTO \"user\"(name, email, password, address, login) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getLogin());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Usuário adicionado!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public User getUser(User userId) {
        try {
            String sql = "";

            sql = "SELECT * FROM \"user\" WHERE id = " + userId.getId();

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            User user = new User();

            while (result.next()) {
                user.setId(result.getInt("id"));
                user.setName(result.getString("name"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                user.setAddress(result.getString("address"));
                user.setLogin(result.getString("login"));
            }

            ps.close();
            result.close();

            if (user.getId() == 0) {
                JOptionPane.showMessageDialog(null, "ID inválido.");
            }

            return user;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar usuário");
            return null;
        }
    }

    public User getByLogin(String login) {
        try {
            String sql = "SELECT * FROM \"user\" WHERE login = ?";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, login);

            ResultSet result = stmt.executeQuery();
            User user = null;

            if (result.next()) {
                user = new User(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("address"),
                        result.getString("login"),
                        null);
            }

            stmt.close();
            result.close();

            return user;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao obter usuário pelo login.");
            return null;
        }
    }

    public boolean identify(String login, String password) {
        try {
            String sql = "SELECT * FROM \"user\" WHERE login = ? AND password = ?";
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setString(1, login);
            stmt.setString(2, password);

            ResultSet result = stmt.executeQuery();
            boolean found = result.next();

            stmt.close();
            result.close();

            return found;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao identificar usuário.");
            return false;
        }
    }

    public ArrayList<User> getAllUsers() {
        try {

            ArrayList<User> data = new ArrayList<User>();

            PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM \"user\"");
            ResultSet result = ps.executeQuery();

            while (result.next()) {

                data.add(new User(
                        result.getInt("id"),
                        result.getString("name"), result.getString("email"), result.getString("password"),
                        result.getString("address"), result.getString("login"), null));

            }
            ps.close();
            result.close();
            this.connection.close();

            return data;
        } catch (SQLException e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Erro ao pegar a lista de usuários.");
            return null;
        }
    }

    public void update(User user) {
        try {
            String sql;
            User oldUser = this.getUser(user);

            sql = "UPDATE \"user\" SET name = ?, email = ?, password = ?, address = ?, login = ? WHERE id = "
                    + user.getId();
            PreparedStatement stmt = this.connection.prepareStatement(sql);

            stmt.setString(1, String.valueOf(user.getName()).isEmpty() ? oldUser.getName()
                    : user.getName());
            stmt.setString(2, String.valueOf(user.getEmail()).isEmpty() ? oldUser.getEmail()
                    : user.getEmail());
            stmt.setString(3, String.valueOf(user.getPassword()).isEmpty() ? oldUser.getPassword()
                    : user.getPassword());
            stmt.setString(4, String.valueOf(user.getAddress()).isEmpty() ? oldUser.getAddress()
                    : user.getAddress());
            stmt.setString(5, String.valueOf(user.getLogin()).isEmpty() ? oldUser.getLogin()
                    : user.getLogin());

            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Usuário atuaizado!");

        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(User user) {
        try {
            if (user == null) {
                JOptionPane.showMessageDialog(null, "Erro: Usuário inválido.");
                return;
            }

            User existingUser = this.getUser(user);

            if (existingUser != null && existingUser.getId() > 0) {
                String sql = "DELETE FROM \"user\" WHERE id = ?";
                try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
                    stmt.setInt(1, user.getId());
                    stmt.execute();
                }
                JOptionPane.showMessageDialog(null, "Usuário excluído com sucesso!");
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
}
