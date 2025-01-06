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
                "jdbc:postgresql://localhost:5432/siricascudo", 
                "postgres", 
                "1234"
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do PostgreSQL não encontrado. Verifique o classpath.", e);
        } catch (SQLException e) {
            throw new SQLException("Erro ao conectar ao banco de dados.", e);
        }
    }
}