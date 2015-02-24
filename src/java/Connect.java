
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author c0644689
 */
public class Connect {

    public static Connection getConnection(){
        
        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String jdbc = "jdbc:mysql://localhost/product";
            String username = "root";
            String password = "";
            con = DriverManager.getConnection(jdbc,username,password);
        }
        catch(ClassNotFoundException | SQLException e){
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE,null,e);
        }
        
        return con;
        
    }
}