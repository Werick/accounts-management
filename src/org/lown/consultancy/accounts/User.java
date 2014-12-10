/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts;

import java.util.List;

/**
 *
 * @author LENOVO USER
 */
public class User {
    private String userName;
    private String name;
    private String password;
    private int userId;
    private List<String> roles;
    
    public boolean hasRole(String role)
    {
        boolean roleok=false;
        for(String s:roles)
        {
            if(s.equalsIgnoreCase(role))
            {
                roleok=true;
                break;
            }
        }
        return roleok;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    
    
}
