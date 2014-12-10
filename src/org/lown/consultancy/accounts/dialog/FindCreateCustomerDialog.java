/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.tables.CustomerListTable;

/**
 *
 * @author LENOVO USER
 */
public class FindCreateCustomerDialog extends JPanel implements ActionListener{

     
    public static final String ACT_FIND="find_search";
    public static final String ACT_CREATE="creat_new";
    public static final String ACT_BACK="exit";
    public static final String ACT_VIEW="view_customer";
    
    public static JDialog dlgFindCreate;
    public static final Font font = new Font("Times New Roman", Font.BOLD, 14);
    public static final Font font2 = new Font("Times New Roman", Font.PLAIN, 14);
    
    
    private JLabel lbl_Title;
    private JLabel lbl_Search;
    private JTextField txt_Search;
    
    private JButton btnBack;
    private JButton btnFind;
    private JButton btnCreate;
    private JButton btnView;
       
    CustomerListTable customerListTable;
    
    public FindCreateCustomerDialog()
    {
         //log info
        AccountsManagement.logger.info("Creating Find/Create UI...");
        dlgFindCreate= new JDialog((JDialog)null, "CUSTOMER CENTER", true);
        dlgFindCreate.setLayout(null);
        dlgFindCreate.setSize(600, 500);
        dlgFindCreate.setLocationRelativeTo(null);
        
        lbl_Title=new JLabel();
        lbl_Title.setBounds(10, 10, 300, 20);         
        lbl_Title.setText("CUSTOMER CENTER");
        lbl_Title.setFont(MainMenu.titleFont);         
        dlgFindCreate.add(lbl_Title);        
        
        lbl_Search=new JLabel();
        lbl_Search.setBounds(10, 40, 350, 20);         
        lbl_Search.setText("Enter Customer's Name or Number or Phone Number:");
        lbl_Search.setFont(font2);     
        dlgFindCreate.add(lbl_Search);
        
        txt_Search=new JTextField();
        txt_Search.setBounds(320, 40, 200, 25);         
        txt_Search.setText(""); 
        //txt_Search.setEditable(false);
        txt_Search.setToolTipText("Enter atleast three characters to Search.");
        txt_Search.setFont(font);
        /*
         * Add a document Listener method to track changes in the text box and fire th search event
         */
        txt_Search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) 
            {
                doSearch();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) 
            {
                doSearch();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                doSearch();
            }
            
            public void doSearch() 
            {
                if (txt_Search.getText().length()>=3)
                {
//                    JOptionPane.showMessageDialog(null,
//                    "Error: Please enter number bigger than 0", "Error Massage",
//                    JOptionPane.ERROR_MESSAGE);
                    //Call the search method
                    customerListTable.insertRow(txt_Search.getText());
                }
            }
           
        });
        dlgFindCreate.add(txt_Search);
        
        btnFind=new JButton("....");
        btnFind.setBounds(520, 40, 30, 25); 
        btnFind.setActionCommand(ACT_FIND);
        btnFind.addActionListener(this);
       // btnSave.setEnabled(false);
        btnFind.setToolTipText("Click to Search customer/company.");
        dlgFindCreate.add(btnFind);
        
        
        
        customerListTable=new CustomerListTable();
        customerListTable.setBounds(0,80,600, 300);
        //findTable.
        dlgFindCreate.add(customerListTable);
        
       btnView=new JButton("View Customer");
       btnView.setBounds(10, 400, 150, 35); 
       btnView.setActionCommand(ACT_VIEW);
       btnView.addActionListener(this); 
       btnView.setToolTipText("Click to View customer/company Dashboard.");
       dlgFindCreate.add(btnView);
       
       btnCreate=new JButton("Create Customer");
       btnCreate.setBounds(200, 400, 150, 35); 
       btnCreate.setActionCommand(ACT_CREATE);
       btnCreate.addActionListener(this);  
       btnCreate.setToolTipText("Click to Create/Add a new customer/company.");
       dlgFindCreate.add(btnCreate);
       
       btnBack=new JButton("Back");
       btnBack.setBounds(400, 400, 150, 35); 
       btnBack.setActionCommand(ACT_BACK);
       btnBack.addActionListener(this);   
       btnBack.setToolTipText("Click to go back the previous window.");
       dlgFindCreate.add(btnBack);
       
       dlgFindCreate.getRootPane().setDefaultButton(btnView);
    }
    
   public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgFindCreate.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_CREATE))
        {
              CustomerListTable.selectedCustomer=null;       
              
              //load registration form
              CustomerDialog customerDialog=new CustomerDialog();
              customerDialog.createAndShowGUI();
	}
        else if(e.getActionCommand().equals(ACT_VIEW))
        {
            if (customerListTable.getRowCount()==0)
            {
                    return;
            }
            else
             {                    
                 //Should Load the Customer Dash Board
                 if(CustomerListTable.selectedCustomer!=null)
                 {
                    //Should Load the Customer Dash Board
                    FindCreateCustomerDialog.dlgFindCreate.setVisible(false); //close find create dialog
                    //SalesDialog invoiceDialog=new SalesDialog();
                    SalesDialog.createAndShowGUI(CustomerListTable.selectedCustomer);                                    
                 }
                    
                }
            return;
	}
        else  if(e.getActionCommand().equals(ACT_FIND))
        {           
            String search;
            search=txt_Search.getText();
            if (search.length()>=3)
            {
                customerListTable.insertRow(search);         
                    
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Enter atleast three characters then click the search/Find Button");
                AccountsManagement.logger.info("Enter atleast three characters then click the search/Find Button...");
            }
            
	}
         
     } 
   
   public void createAndShowGUI()
     {
         dlgFindCreate.setVisible(true);          
         dlgFindCreate.dispose(); //close the app once soen
     }
}
