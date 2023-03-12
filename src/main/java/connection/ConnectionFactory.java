package connection;
import java.sql.*;

public class ConnectionFactory {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/ordersdb";
    private static final String USER = "root";
    private static final String PASS = "oanasql23";
    private final String DATABASENAME = "ordersdb";
    private Connection connection=null;

    /**
     * Creates a new instance of ConnectionFactory.
     * makes the connection and prints an error message in case of failure.
     */
    public ConnectionFactory()
    {
        try{
            connection= DriverManager.getConnection(DBURL,USER,PASS);
        }
        catch (SQLException exception) {

            System.out.println("An error occurred while connecting SQL databse");
            exception.printStackTrace();
        }
    }

    /**
     * @return the connection.
     */
    public Connection getConnection() {
        return connection;
    }


    public String getDATABASENAME() {
        return DATABASENAME;
    }


    /**
     * Attempts to close the established connection with the database.
     * @return 0 if succes, -1 if an error occured.
     */
    public int closeConnection()
    {
        try{
            if(connection!=null && !connection.isClosed())
            {
                connection.close();
                return 0;
            }
            return -1;
        }
        catch(SQLException e)
        {
            System.out.println("Error closing connection\n");
            return -1;
        }
    }
}

