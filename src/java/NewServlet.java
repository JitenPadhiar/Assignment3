/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author Jiten
 */
@WebServlet("/NewServlet")
public class NewServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            Connection conn = Connect.getConnection();

            if (!request.getParameterNames().hasMoreElements()) {
                // There are no parameters at all
                out.println(getResults("SELECT * FROM product"));
            } else {
                // There are some parameters
                int id = Integer.parseInt(request.getParameter("id"));
                out.println(getResults("SELECT * FROM product WHERE productId = ?", String.valueOf(id)));

            }

        } catch (IOException ex) {

        }

    }

    private String getResults(String query, String... params) {

        StringBuilder sb = new StringBuilder();

        try (Connection conn = Connect.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(query);

            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }

            ResultSet rs = pstmt.executeQuery();
            JSONObject obj = new JSONObject();
            while (rs.next()) {

                obj.put("ProductID", rs.getInt("productId"));
                obj.put("Name", rs.getString("name"));
                obj.put("Description", rs.getString("description"));
                obj.put("Quantity", rs.getInt("quantity"));
                sb.append(obj.toJSONString());
            }

        } catch (SQLException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }
    
    
      @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        
        Set<String> keySet = request.getParameterMap().keySet();
        
        try (PrintWriter out = response.getWriter()) {
            if (keySet.contains("name") && keySet.contains("description") && keySet.contains("quantity")) {
                            
                String name = request.getParameter("name");
                String quantity = request.getParameter("quantity");
                String description = request.getParameter("description");
                
                doUpdate("INSERT INTO product (name,description,quantity) VALUES (?,?,?)", name,description, quantity);
            } else {
                response.setStatus(500);
            }
        } catch (IOException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        
        Set<String> keySet = request.getParameterMap().keySet();
        
        try (PrintWriter out = response.getWriter()) {
            if (keySet.contains("name") && keySet.contains("description") && keySet.contains("quantity") && keySet.contains("productId") ) {
                 
                String id = request.getParameter("productId");
                String name = request.getParameter("name");
                String quantity = request.getParameter("quantity");
                String description = request.getParameter("description");
                
                doUpdate("UPDATE product SET name=?,description=?,quantity=? where productId=? ",name,description,quantity,id);
            } else {
                response.setStatus(500);
            }
        } catch (IOException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
     protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        
        Set<String> keySet = request.getParameterMap().keySet();
        
        try (PrintWriter out = response.getWriter()) {
            if (keySet.contains("productId") ) {
                 
                String id = request.getParameter("productId");
                                
                doUpdate("DELETE FROM product where productId=? ",id);
            } else {
                response.setStatus(500);
            }
        } catch (IOException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void doUpdate(String query, String... params) {
        
        try (Connection conn = Connect.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
           pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }

}
