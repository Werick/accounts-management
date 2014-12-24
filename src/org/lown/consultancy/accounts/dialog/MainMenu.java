/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.User;
import org.lown.consultancy.accounts.dao.CompanyDAO;
import org.lown.consultancy.accounts.dao.PDFWriter;
import org.lown.consultancy.accounts.dao.StockDAO;

/**
 *
 * @author LENOVO USER
 */
public class MainMenu extends JFrame implements ActionListener{
    
    //action commants for various command buttons
    public static final String ACT_EXIT="Exit_Application";
    public static final String ACT_PRODUCT="Manage_Product";
    public static final String ACT_CUSTOMER="Manage_Customer";
    public static final String ACT_SUPPLIER="Manage_Supplier";
    public static final String ACT_CATEGORY="product_category";
    public static final String ACT_USER="Manage_User";
    public static final String ACT_STOCK="Manage_Stock";
    public static final String ACT_TREASURY="Manage_Bank_Cash";
    public static final String ACT_REPORT="Manage_Report";
    public static final String ACT_GLOBALPROPERTIES="Manage_Properties";
    public static final Font titleFont = new Font("Times New Roman", Font.BOLD, 20);
    public static final Font title2Font = new Font("Times New Roman", Font.BOLD, 14);
    
    private JDialog menuDialog;
    private JPanel pMenu,pQuickLinks,pStatus;
    private JLabel title;
    private JLabel title2;
    private JLabel title3;
    private JButton btnExit;
    private JButton btnCustomer;
    private JButton btnSupplier;    
    private JButton btnTreasury;
    private JButton btnStock;
    private JButton btnUser;
    private JButton btnProduct;
    private JButton btnReport;
    private JButton btnGlobalProperty;
    private JLabel txt_User;
    public static User gUser;
    public static CompanyDAO cs;
    public static StockDAO ss;
    public static Map companyDetails;
    
    private JLabel lbl_creditorInvoice;
    private JLabel lbl_creditorPendingInvoice;
    private JLabel lbl_debtorInvoice;
    private JLabel lbl_debtorPendingInvoice;
    private JLabel lbl_stock;
    private JLabel lbl_developer;
    
    private TitledBorder titled1 = new TitledBorder("Aplication Main Menu");
    private TitledBorder titled2 = new TitledBorder("Quick Links");
    private String workingDir = System.getProperty("user.dir");
    
    public MainMenu(User user)
    {
        gUser=user;
        cs=new CompanyDAO();
        ss=new StockDAO();
        companyDetails=cs.getCompanyDetails();
        /*
         * Will prefer to user Global properties for some standard stuff
         * E.g. The company Name
         * Database Conncetion Details
         */
        String company=(String)companyDetails.get("company.name");
        String address=(String)companyDetails.get("company.address");
        
        menuDialog= new JDialog((JDialog)null, company, true);        
        menuDialog.setLayout(null);
        menuDialog.setSize(1400, 850); //creating the size of frame
        menuDialog.setLocationRelativeTo(null);//center the dialog
        
        titled1.setTitleFont(title2Font);
        titled2.setTitleFont(title2Font);
        
        
        pStatus=new JPanel();
        pStatus.setBounds(550, 750, 800, 50);
        //pStatus.setBorder(titled2);  
        pStatus.setLayout(null);
        menuDialog.add(pStatus);
        
        txt_User=new JLabel();
        txt_User.setBounds(500,10,200,25);
        txt_User.setText("Current User:  "+gUser.getName());
        txt_User.setFont(title2Font);
        txt_User.setHorizontalAlignment(JLabel.RIGHT);        
        pStatus.add(txt_User);
        
        lbl_developer=new JLabel();
        lbl_developer.setBounds(10,10,400,25);
        lbl_developer.setText("Developer: LOWN Consultancy (copyright@2014)  ");
        lbl_developer.setFont(title2Font);
        pStatus.add(lbl_developer);
        
        pQuickLinks=new JPanel();
        pQuickLinks.setBounds(950, 50, 400, 650);
        pQuickLinks.setBorder(titled2);  
        pQuickLinks.setLayout(null);
        menuDialog.add(pQuickLinks);
        
        //quick shortcut links
        lbl_creditorInvoice=new JLabel();
        lbl_creditorInvoice.setBounds(20, 50, 300, 25);         
        lbl_creditorInvoice.setText("Overdue Supplier Invoices");
        lbl_creditorInvoice.setFont(title2Font);
        pQuickLinks.add( lbl_creditorInvoice);
        
        lbl_creditorPendingInvoice=new JLabel();
        lbl_creditorPendingInvoice.setBounds(20, 100, 300, 25);         
        lbl_creditorPendingInvoice.setText("Pending Supplier Invoices");
        lbl_creditorPendingInvoice.setFont(title2Font);
        pQuickLinks.add( lbl_creditorPendingInvoice);
        
        lbl_debtorInvoice=new JLabel();
        lbl_debtorInvoice.setBounds(20, 150, 300, 25);         
        lbl_debtorInvoice.setText("Overdue Debtor/Customer Invoices");
        lbl_debtorInvoice.setFont(title2Font);
        pQuickLinks.add(lbl_debtorInvoice);
        
        lbl_debtorPendingInvoice=new JLabel();
        lbl_debtorPendingInvoice.setBounds(20, 200, 300, 25);         
        lbl_debtorPendingInvoice.setText("Pending Debtor/Customer Invoices");
        lbl_debtorPendingInvoice.setFont(title2Font);
        pQuickLinks.add( lbl_debtorPendingInvoice);
        
        lbl_stock=new JLabel();
        lbl_stock.setBounds(20, 250, 300, 25);         
        lbl_stock.setText("Stock Items Due for Ordering: ("+ss.getPendingStockItems()+")");
        lbl_stock.setFont(title2Font);
        pQuickLinks.add(lbl_stock);
        
        
        pMenu=new JPanel();
        pMenu.setBounds(40, 50, 300, 670);
        pMenu.setBorder(titled1);  
        pMenu.setLayout(null);
        menuDialog.add(pMenu);
        
        title=new JLabel();
        title.setBounds(300, 10, 800, 20);         
        title.setText(company+" ACCOUNT'S RECEIVABLE/PAYABLE MAIN MENU");
        title.setFont(titleFont);
         //title.setAlignmentX(TOP_ALIGNMENT);
        menuDialog.add(title);
        
        title2=new JLabel();
        title2.setBounds(600, 40, 400, 20);         
        title2.setText(address);
        title2.setFont(titleFont);
         //title.setAlignmentX(TOP_ALIGNMENT);
        menuDialog.add(title2);
        
        //images
          //load/display image in a label
       ImageIcon image=new ImageIcon(workingDir+"\\src\\org\\lown\\consultancy\\accounts\\images\\forster.png");
       title3 = new JLabel();
       title3.setBounds(350,100,580,450);//some random value that I know is in my dialog
       title3.setHorizontalAlignment(JLabel.CENTER);
       title3.setVerticalAlignment(JLabel.TOP);
       title3.setIcon(image);         
       menuDialog.add(title3);
         
       btnCustomer=new JButton("Manage Customers");
       btnCustomer.setBounds(50, 30, 200, 60);
       btnCustomer.setActionCommand(ACT_CUSTOMER);
       btnCustomer.addActionListener(this);
       btnCustomer.setToolTipText("Create, Find, Edit Customers and Carry out all customer transactions");
       pMenu.add(btnCustomer);
         
         
       btnProduct=new JButton("Manage Products");
       btnProduct.setBounds(50, 100, 200, 60);
       btnProduct.setActionCommand(ACT_PRODUCT);
       btnProduct.addActionListener(this);
       btnProduct.setToolTipText("Manage Prouct and Product Categories");
       pMenu.add(btnProduct);         
       
       btnSupplier=new JButton("Manage Suppliers");
       btnSupplier.setBounds(50, 170, 200, 60);
       btnSupplier.setActionCommand(ACT_SUPPLIER);
       btnSupplier.addActionListener(this);
       pMenu.add(btnSupplier);
         
       btnStock=new JButton("Manage Stock");
       btnStock.setBounds(50, 240, 200, 60);
       btnStock.setActionCommand(ACT_STOCK);
       btnStock.addActionListener(this);
       btnStock.setToolTipText("Manage Stock Prices and Quantities");
       pMenu.add(btnStock);
       
       btnReport=new JButton("Manage Reports");
       btnReport.setBounds(50, 310, 200, 60);
       btnReport.setActionCommand(ACT_REPORT);
       btnReport.addActionListener(this);
       pMenu.add( btnReport);
       
       btnUser=new JButton("User Management");
       btnUser.setBounds(50, 380, 200, 60);
       btnUser.setActionCommand(ACT_USER);
       btnUser.addActionListener(this);
       pMenu.add( btnUser);
         
       btnTreasury=new JButton("Manage Treasury");
       btnTreasury.setBounds(50, 450, 200, 60);
       btnTreasury.setActionCommand(ACT_TREASURY);
       btnTreasury.addActionListener(this);
       pMenu.add( btnTreasury);
         
       btnGlobalProperty=new JButton("Manage Global Properties");
       btnGlobalProperty.setBounds(50, 520, 200, 60);
       btnGlobalProperty.setActionCommand(ACT_GLOBALPROPERTIES);
       btnGlobalProperty.addActionListener(this);
       btnGlobalProperty.setToolTipText("Manage Application's Global Properties");
       pMenu.add(btnGlobalProperty);
       
       
       btnExit=new JButton("Exit");
       btnExit.setBounds(50, 590, 200, 60);
       btnExit.setActionCommand(ACT_EXIT);
       btnExit.addActionListener(this);
       pMenu.add(btnExit);
       
       
       menuDialog.getRootPane().setDefaultButton(btnCustomer);
       
       //set authorization
       if(!user.hasRole("admin"))
       {
           btnGlobalProperty.setEnabled(false);
       }
        
       menuDialog.setVisible(true); //making the frame visible
       menuDialog.dispose(); //close the app once done
    }
    
     public static void createAndShowGUI(User user) 
     {
		MainMenu paneContent = new MainMenu(user);
     }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().equals(ACT_EXIT)){
            //log info
            AccountsManagement.logger.info("Exiting Accounts Management Application... ");
            menuDialog.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_GLOBALPROPERTIES))
        {
            AccountsManagement.logger.info("Loading global properties form form... ");            
            PDFWriter pdfWriter=new PDFWriter(); 
            return;
	}
        else if(e.getActionCommand().equals(ACT_TREASURY))
        {
            AccountsManagement.logger.info("Loading Treasury Dashboard Dialog form... ");            
            TreasuryDialog.createAndShowGUI(); 
            return;
	}
        else if(e.getActionCommand().equals(ACT_CUSTOMER))
        {
            AccountsManagement.logger.info("Loading Customer Dashboard Dialog form... ");            
            CustomerDashboard.createAndShowGUI(); 
            return;
	}
        else  if(e.getActionCommand().equals(ACT_CATEGORY))
        {
            AccountsManagement.logger.info("Loading Product Categories Dialog form... ");            
            CategoryDialog.createAndShowGUI(); 
            return;
        }
        else  if(e.getActionCommand().equals(ACT_PRODUCT))
        {
            AccountsManagement.logger.info("Loading Product Dashboard Dialog form... ");            
            ProductDashboard.createAndShowGUI(); 
            return;
        }
        else  if(e.getActionCommand().equals(ACT_SUPPLIER))
        {
            AccountsManagement.logger.info("Loading Supplier Dialog form... ");            
//            SupplierDialog.createAndShowGUI(); 
            SupplierDashboard.createAndShowGUI();
            return;
        }
        else  if(e.getActionCommand().equals(ACT_STOCK))
        {
            AccountsManagement.logger.info("Loading Stock Dialog form... "); 
            StockItemDialog.createAndShowGUI();
            return;
        }
        else  if(e.getActionCommand().equals(ACT_USER))
        {
            AccountsManagement.logger.info("Loading Users Dialog form... ");            
            UserDialog.createAndShowGUI();            
            return;
        }
    }
    
}
