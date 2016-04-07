package BDD.db;

//Step 1: Use interfaces from java.sql package
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectonJDBC {
    //private static String ip = "148.60.13.86";

    //private static String ip = "192.168.0.11";
    //Maestro:
    private static String ip = "192.168.103.1";
    private static String port = "3306";

    //static reference to itself
    private static final ConnectonJDBC instance = new ConnectonJDBC();

    private static final String URL = "jdbc:mysql://"+ip+":"+port+"/emaestro";

    private static final String USER = "android";
    private static final String PASSWORD = "mdp";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    //private constructor
    public ConnectonJDBC() {

        try {
            //Step 2: Load MySQL Java driver
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }

    private Connection createConnection() {

        Connection connection = null;
        try {
            //Step 3: Establish Java MySQL connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to Connect to Database.");
            System.out.println(e.toString());

        }
        return connection;
    }

    public static Connection getConnection() {
        return instance.createConnection();
    }


}