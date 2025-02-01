package database;

import java.sql.*;

public class JDBConnectionWrapper {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL= "jdbc:mysql://localhost/";
    private static final String USER = "root";
    private static final String PASSWORD = "parola";
    private static final int TIMEOUT = 5;
    private Connection connection;


    public JDBConnectionWrapper(String schema){
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL + schema, USER, PASSWORD );
            createTables();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    private void  createTables() throws SQLException {
        Statement statement= connection.createStatement();

        String sql= "CREATE TABLE IF NOT EXISTS book(" +
                " id bigint NOT NULL AUTO_INCREMENT," +
                " author VARCHAR(500) NOT NULL," +
                " title VARCHAR(500) NOT NULL," +
                " publishedDate datetime DEFAULT NULL," +
                " price DECIMAL(10,2) NOT NULL," +
                " stock INT NOT NULL, " +
                " PRIMARY KEY(id)," +
                " UNIQUE KEY id_UNIQUE(id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);

//        String sqlSoldBooks="CREATE TABLE IF NOT EXISTS sold_books (" +
//                " id INT NOT NULL AUTO_INCREMENT," +
//                "  author varchar(500) NOT NULL," +
//                "  title varchar(500) NOT NULL," +
//                "  price DECIMAL(10,2) NOT NULL," +
//                "  stock_sold INT NOT NULL," +
//                "  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
//                "  user_id bigint NOT NULL," +
//                "  PRIMARY KEY (id)," +
//                "  UNIQUE KEY id_UNIQUE (id), " +
//                "  CONSTRAINT fk_user_id "+
//                "  FOREIGN KEY (user_id)" +
//                "  REFERENCES user(id)" +
//                "  ON DELETE CASCADE" +
//                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
//
//        statement.execute(sqlSoldBooks);

    }

    public boolean testConnection() throws SQLException {
        return connection.isValid(TIMEOUT);
    }

    public Connection getConnection(){
        return connection;
    }
}
