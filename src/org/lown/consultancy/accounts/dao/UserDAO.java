/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.User;

/**
 *
 * @author LENOVO USER
 */
public class UserDAO {
    
    //private Sql Sql;
    public UserDAO()
    {
        //Sql=new Sql();
    }
    /**
     * get User given the user name and password
     * @param uName
     * @param pass
     * @return user
     */
    public User getUser(String uName, String pass) 
    {
        User user=null;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting user Details given username and pass... ");
            String sqlStmt="Select UserID,lname,username from users WHERE username ='" + uName + "' and pass=password('"+ pass + "');";
           // Open();
           // Statement statement = c.createStatement();
            ResultSet rs2=Sql.executeQuery(sqlStmt);
            while (rs2.next())
            {
                user=new User();
                user.setUserId(rs2.getInt("UserID"));  
                user.setName(rs2.getString("lname"));
                user.setUserName(rs2.getString("username"));              
            }
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.fillInStackTrace());
         }  
        return user;
    }
    
    /**
     * get User user roles
     * @param User user     
     * @return List<String> roles
     */
    public List<String> getUserRole(User user) 
    {
        List<String> roles=new ArrayList<String>();
        try
        {
            //log info
            AccountsManagement.logger.info("Getting user Details given username and pass... ");
            String sqlStmt="select u.userid,r.role ";
                    sqlStmt+="from user_role u ";
                    sqlStmt+="join role r on r.role_id=u.role_id ";
                    sqlStmt+="where u.voided=0 and u.userid ="+user.getUserId();
           // Open();
           // Statement statement = c.createStatement();
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                roles.add(rs.getString("role"));              
            }
            
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.fillInStackTrace());
         }  
        return  roles;
    }
}
