import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class conexao {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=padaria;encrypt=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "123";

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
