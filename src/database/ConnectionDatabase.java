package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDatabase {
    public Connection getConnection() throws SQLException {
        try {
            // Carregar o driver (opcional com versões recentes do JDBC)
            Class.forName("org.postgresql.Driver");
            
            // Estabelecer a conexão
            return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/siriCascudo", 
                "postgres", 
                "1234"
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do PostgreSQL não encontrado. Verifique o classpath.", e);
        } catch (SQLException e) {
            throw new SQLException("Erro ao conectar ao banco de dados.", e);
        }
    }

    public static void main(String[] args) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        try {
            Connection connection = connectionDatabase.getConnection();
            System.out.println("Conexão estabelecida com sucesso!");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}