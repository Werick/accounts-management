/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.User;
import org.lown.consultancy.accounts.dialog.LoginDialog;
import org.lown.consultancy.accounts.dialog.MainMenu;

/**
 *
 * @author LENOVO USER
 */
public class UserDAO {
    
    private String preppedStmtInsert=null;
    private String preppedStmtUpdate=null;
    
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
    
    public Map<String,Integer> getUserRoleMap()
    {
        Map<String,Integer>rolesMap=new HashMap<String,Integer>();
                try
        {
            //log info
            AccountsManagement.logger.info("Getting user roles.. ");
            String sqlStmt="select r.role_id,r.role ";
                    sqlStmt+="from role r ";                    
          
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                rolesMap.put(rs.getString("role"),rs.getInt("role_id"));              
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
                
                return rolesMap;

    }
    public User getUser(int id) 
    {
        User user=null;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting user Details given username and pass... ");
            String sqlStmt="Select UserID,lname,username, othername, pass from users WHERE userid =" + id + ";";
           // Open();
           // Statement statement = c.createStatement();
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                user=new User();
                user.setUserId(rs.getInt("UserID"));  
                user.setName(rs.getString("lname"));
                user.setUserName(rs.getString("username"));   
                user.setOtherNames(rs.getString("othername"));
                user.setPassword(rs.getString("pass"));                
            }
            rs.close();
            user.setRoles(getUserRole(user));
            
            
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
    
    public List<User> getAllUsers() 
    {
        List<User> usersList=new ArrayList<User>();
        try
        {
            //log info
            AccountsManagement.logger.info("Getting user Details given username and pass... ");
            String sqlStmt="SELECT u.userid,lname,username, group_concat(r.role order by r.role)as roles ";
                    sqlStmt+="FROM users u ";
                    sqlStmt+="join user_role ur on ur.userid=u.userid and ur.voided=0 ";
                    sqlStmt+="join role r on r.role_id=ur.role_id ";
                    sqlStmt+="where u.voided=0 ";
                    sqlStmt+="group by u.userid; ";
                    
           // Open();
           // Statement statement = c.createStatement();
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                User user=new User();
                user.setUserId(rs.getInt("userid"));
                user.setName(rs.getString("lname"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("roles"));
                usersList.add(user);              
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
        return  usersList;
    }
    
    public void saveUser(User user) throws SQLException{
        try
        {
             //Sql.Open();
            Sql.getConnection().setAutoCommit(false) ;
            /*
             * Instert the transaction summary then followed by individual transaction items
             */
            
            preppedStmtInsert="INSERT INTO users (lname,othername,username,pass,dateCreated,createdby) VALUES(?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);   
            pst.setString(1, user.getName());
            pst.setString(2, user.getOtherNames());
            pst.setString(3, user.getUserName());
            pst.setString(4, user.getPassword());
            pst.setTimestamp(5, Sql.getCurrentTimeStamp());
            pst.setInt(6, MainMenu.gUser.getUserId());             
            pst.execute();    
            
            int autoIncKeyFromApi = -1; 
            ResultSet rs = pst.getGeneratedKeys(); 
            if (rs.next()) 
            {
                autoIncKeyFromApi = rs.getInt(1);
                //System.out.println("Key returned from getGeneratedKeys():" + autoIncKeyFromApi);
            } 
            else 
            {
                // throw an exception from here
            }
            rs.close(); 
            rs = null; 
            int user_id=autoIncKeyFromApi;
            /*
             * Posting individual transaction items
             */
            for(String s:user.getRoles())
            {
                preppedStmtInsert="INSERT INTO user_role (role_id,userid,dateCreated,createdby) VALUES(?,?,?,?)";
                pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);                
               
                pst.setInt(1, LoginDialog.rolesMap.get(s));                
                pst.setInt(2, user_id);                           
                pst.setTimestamp(3, Sql.getCurrentTimeStamp());
                pst.setInt(4, MainMenu.gUser.getUserId()); 
                pst.execute();
            }
            
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "User successfully Saved...");
            
        }
        catch(SQLException e)
        {
             // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            //Log error
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.toString());
            Sql.getConnection().rollback();
        }
    }
    
    /**
     * Update User details
     * @param User user
     */
    public void updateUser(User user) throws SQLException 
    {
          
         try
         {
             //log info
             AccountsManagement.logger.info("Updating User Details... ");
             
             Sql.getConnection().setAutoCommit(false) ;
             preppedStmtUpdate="update users set lname=?,othername=?,username=?,pass=?,dateChanged=?, changedBy=? WHERE userid=?";                    
             PreparedStatement pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
             
             pst.setString(1,user.getName());
             pst.setString(2,user.getOtherNames());
             pst.setString(3,user.getUserName());
             pst.setString(4,user.getPassword());
             pst.setTimestamp(5, Sql.getCurrentTimeStamp()); 
             pst.setInt(6, MainMenu.gUser.getUserId());
             pst.setInt(7, user.getUserId());
             pst.executeUpdate();
       
             //get current user roles
             List<String>curRoles=getUserRole(user);
             //we want to update only if the user has new roles or has been strucked off is current roles
             
             //Insert any new role
             for(String s:user.getRoles())
             {
                 if(!curRoles.contains(s))
                 {
                     preppedStmtInsert="INSERT INTO user_role (role_id,userid,dateCreated,createdby) VALUES(?,?,?,?)";
                     pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);                
               
                     pst.setInt(1, LoginDialog.rolesMap.get(s));                
                     pst.setInt(2, user.getUserId());                           
                     pst.setTimestamp(3, Sql.getCurrentTimeStamp());
                     pst.setInt(4, MainMenu.gUser.getUserId()); 
                     pst.execute();
                 }
             }
             
             //Struck user roles
             for(String s: curRoles)
             {
                 if(s.equalsIgnoreCase("System Developer")) //skip if has system developer role
                 {
                     continue;
                 } 
                 if(!user.getRoles().contains(s) )
                 {
                     preppedStmtUpdate="update user_role set voided=?,datevoided=?, voidedby=?, voidreason=? WHERE userid=? and role_id=?";                    
                     pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
                     
                     pst.setInt(1,1);
                     pst.setTimestamp(2, Sql.getCurrentTimeStamp()); 
                     pst.setInt(3, MainMenu.gUser.getUserId());
                     pst.setString(4,"Dropping user role");
                     pst.setInt(5, user.getUserId());
                     pst.setInt(6, LoginDialog.rolesMap.get(s));    
                     pst.executeUpdate();
                 }
             }
             
             Sql.getConnection().commit();
             //commit transaction if successful
             JOptionPane.showMessageDialog(null, "User Record Updated...");
                          
         }
         catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            //Log error
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.toString());
            Sql.getConnection().rollback();
         }
         
    }
}
