/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.tables;

import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.lown.consultancy.accounts.User;
import org.lown.consultancy.accounts.dao.UserDAO;
import org.lown.consultancy.accounts.dialog.UserDialog;

/**
 *
 * @author LENOVO USER
 */
public class UsersTable extends JPanel{
    private JTable jTable;
    private String[] columnTitle=new String[]{"user_id","Names ","User Name","User roles"};
    private static DefaultTableModel model ;
    private Object[][] data;     
    private UserDAO userDAO;    
    private List<User> usersList;
    public static User selectedUser;    
    
    public static int selectedUserId;
    
    public UsersTable()
    {
        model = new DefaultTableModel(data,columnTitle);
        
        userDAO=new UserDAO();
        usersList=userDAO.getAllUsers();
        selectedUser=null;
        jTable = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
            return false; //Disallow the editing of any cell
            }
        };
        jTable.setSize(600, 150); 
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.setRowHeight(25);
        jTable.setBorder(BorderFactory.createTitledBorder(""));
        
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setFillsViewportHeight(true);
        JTableHeader header = jTable.getTableHeader();
        getSelectedRow();
        setPreferredSize(new Dimension(600, 150));       
        jTable.setPreferredScrollableViewportSize(new Dimension(600, 150));
        
        //set coulmun size
        TableColumn column = null;
        column = jTable.getColumnModel().getColumn(0); //Id
        column.setPreferredWidth(20);
        
        column = jTable.getColumnModel().getColumn(1); //Code column
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(2); //Name column
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(3); //Name column
        column.setPreferredWidth(200);
             
        add(scrollPane);
        add(header);
        validate();
    }
    
    public void getSelectedRow()
    {
     //  JOptionPane.showMessageDialog(null, "Selected Row is: "+jTable.getSelectedRow());
       jTable.getSelectionModel().addListSelectionListener(
               new ListSelectionListener(){
                   public void valueChanged(ListSelectionEvent e)
                   {
                       int selectedRow=jTable.getSelectedRow();
                       if ((selectedRow>=0)&&(jTable.getRowCount()>selectedRow))
                       {
                            Object data = model.getValueAt(selectedRow, 0);
                            int modelRow = jTable.convertRowIndexToModel(selectedRow);
                            System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                            System.out.println(String.format("Selected Row in view:  " + data.toString()));
                            
                            //selectedAccount=cs.getContraAccountByName(data.toString())   ;  
                            selectedUserId=Integer.parseInt(data.toString());
                            selectedUser=userDAO.getUser(selectedUserId);
                            if (selectedUser!=null)
                            {
                                UserDialog.txt_sirName.setText(selectedUser.getName());
                                UserDialog.txt_otherNames.setText(selectedUser.getOtherNames());
                                UserDialog.txt_userName.setText(selectedUser.getUserName());
                                UserDialog.txt_pass.setText(selectedUser.getPassword());
                                UserDialog.txt_confirmPass.setText(selectedUser.getPassword());
                                
                                //reset
                                UserDialog.chk_accountant.setSelected(false);
                                UserDialog.chk_admin.setSelected(false);
                                UserDialog.chk_clerk.setSelected(false);
                                UserDialog.chk_authenticated.setSelected(false);
                                
                                
                                for(String s: selectedUser.getRoles())
                                {
                                    System.out.println(s);
                                    if(s.equalsIgnoreCase(UserDialog.chk_accountant.getText()))
                                    {
                                        UserDialog.chk_accountant.setSelected(true);
                                    }
                                   
                                    
                                    if(s.equalsIgnoreCase(UserDialog.chk_admin.getText()))
                                    {
                                        UserDialog.chk_admin.setSelected(true);
                                    }
                                    
                                            
                                    
                                    if(s.equalsIgnoreCase(UserDialog.chk_clerk.getText()))
                                    {
                                        UserDialog.chk_clerk.setSelected(true);
                                    }
                                    
                                    
                                    if(s.equalsIgnoreCase(UserDialog.chk_authenticated.getText()))
                                    {
                                        UserDialog.chk_authenticated.setSelected(true);
                                    }
                                    
                                }
                            }
                       }
                       else
                       {
                           selectedUserId=0;
                       }
                   }
                   
               });
    }
    
     public void insertRow()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
         
        //list all available product categories from the database
        if(!usersList.isEmpty())
         {
            for (User u:usersList)
            {
                //System.out.println("Testing Stuff"+c.getCustomerNumber());
                
                model.insertRow(jTable.getRowCount(),new Object[]{u.getUserId(), u.getName(),u.getUserName(),u.getPassword()});
             }
          }

    }
}
