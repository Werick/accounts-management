/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.dao.CustomerDAO;
import org.lown.consultancy.accounts.dao.PDFWriter;
import org.lown.consultancy.accounts.dao.StockDAO;
import org.lown.consultancy.accounts.dao.SupplierDAO;

/**
 *
 * @author LENOVO USER
 */
public class ReportsDialog extends JPanel implements ActionListener{
    private static final String ACT_BACK="close";
    
    public static final Font titleFont = new Font("Times New Roman", Font.PLAIN, 14);
    
    private static JDialog dlgReport;
    private JPanel pReports;
    
    private JButton btnClose;
    
    private TitledBorder titled2 = new TitledBorder("List of Reports");
    public static final Font title2Font = new Font("Times New Roman", Font.BOLD, 14);
    
    
    private JLabel lbl_customer;
    private JLabel lbl_stock;
    private JLabel lbl_supplier;
    private JLabel lbl_sales;
    private JLabel lbl_purchases;
    private JLabel lbl_cash;
    private JLabel lbl_user;
    private JLabel lbl_debts;
    private JLabel lbl_creditors;
    
    
    private PDFWriter pdfWriter; 
    private CustomerDAO custDAO;
    private SupplierDAO supDAO;
    private StockDAO stockDAO;
    private SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy"); 
    
    public ReportsDialog()
    {
        dlgReport= new JDialog((JDialog)null, "View/Print Reports", true);
        dlgReport.setLayout(null);
        dlgReport.setSize(750, 500);//Width size, Height size
        dlgReport.setLocationRelativeTo(null);//center the invoice on the screen
        
        pReports=new JPanel();
        pReports.setBounds(20, 10, 700, 390);
        pReports.setBorder(titled2);  
        pReports.setLayout(null);
        dlgReport.add(pReports);
        
        lbl_customer=new JLabel();
        lbl_customer.setBounds(20, 20, 300, 25);         
        lbl_customer.setText("List of Customers");
        lbl_customer.setFont(titleFont);
        lbl_customer.setToolTipText("Click to view List of Customers");
        lbl_customer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl_customer.addMouseListener(new MListener());
        pReports.add(lbl_customer);
        
        lbl_debts=new JLabel();
        lbl_debts.setBounds(450, 20, 300, 25);         
        lbl_debts.setText("List of Pending Debtors");
        lbl_debts.setFont(titleFont);
        lbl_debts.setToolTipText("Click to view Pending Debtors");
        lbl_debts.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl_debts.addMouseListener(new MListener());
        pReports.add(lbl_debts);
        
        lbl_supplier=new JLabel();
        lbl_supplier.setBounds(20, 70, 300, 25);         
        lbl_supplier.setText("List of Suppliers");
        lbl_supplier.setFont(titleFont);
        lbl_supplier.setToolTipText("Click to view List of Suppliers");
        lbl_supplier.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl_supplier.addMouseListener(new MListener());
        pReports.add(lbl_supplier);
        
        lbl_creditors=new JLabel();
        lbl_creditors.setBounds(450, 70, 300, 25);         
        lbl_creditors.setText("List of Pending Creditors");
        lbl_creditors.setFont(titleFont);
        lbl_creditors.setToolTipText("Click to view Pending Creditors");
        lbl_creditors.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl_creditors.addMouseListener(new MListener());
        pReports.add(lbl_creditors);
        
        lbl_stock=new JLabel();
        lbl_stock.setBounds(20, 120, 300, 25);         
        lbl_stock.setText("List of Stock Items");
        lbl_stock.setFont(titleFont);
        lbl_stock.setToolTipText("Click to view List of Stock Items");
        lbl_stock.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        lbl_stock.addMouseListener(new MListener());
        pReports.add(lbl_stock);
        
        lbl_sales=new JLabel();
        lbl_sales.setBounds(20, 170, 300, 25);         
        lbl_sales.setText("List of Sales");
        lbl_sales.setFont(titleFont);
        lbl_sales.setToolTipText("Click to view List of Sales");
        lbl_sales.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        lbl_sales.addMouseListener(new MListener());
        pReports.add(lbl_sales);
        
        lbl_purchases=new JLabel();
        lbl_purchases.setBounds(20, 220, 300, 25);         
        lbl_purchases.setText("List of Purchases");
        lbl_purchases.setFont(titleFont);
        lbl_purchases.setToolTipText("Click to view List of Purchases");
        lbl_purchases.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        lbl_purchases.addMouseListener(new MListener());
        pReports.add(lbl_purchases);
        
        lbl_cash=new JLabel();
        lbl_cash.setBounds(20, 270, 300, 25);         
        lbl_cash.setText("List of Cash Transactions");
        lbl_cash.setFont(titleFont);
        lbl_cash.setToolTipText("Click to view List of Cash Transactions");
        lbl_cash.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        lbl_cash.addMouseListener(new MListener());
        pReports.add(lbl_cash);
        
        lbl_user=new JLabel();
        lbl_user.setBounds(20, 320, 300, 25);         
        lbl_user.setText("List of Users");
        lbl_user.setFont(titleFont);
        lbl_user.setToolTipText("Click to view List of Users");
        lbl_user.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        lbl_user.addMouseListener(new MListener());
        pReports.add(lbl_user);
        
        btnClose=new JButton("Back");
        btnClose.setBounds(570, 410, 150, 40);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgReport.add(btnClose);        
        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgReport.setVisible(false);
            return;
	}
    }
    
    public static void createAndShowGUI()
    {
         AccountsManagement.logger.info("Loading Treasury Dialog");
         ReportsDialog reportDialog = new ReportsDialog();      
         
         dlgReport.setVisible(true);          
         dlgReport.dispose(); //close the app once done
    }
    
    //adding an inner class for Mouse Listener
    class MListener extends MouseAdapter
    {
        @Override
        public void mouseReleased(MouseEvent e) {}
         
        @Override
        public void mousePressed(MouseEvent e) 
         {
             //System.out.println("Pressed!");
             //add any code
         }

        @Override
        public void mouseExited(MouseEvent e) 
        {
            //System.out.println("Exited!");
            //add any code
        }

        @Override
        public void mouseEntered(MouseEvent e) 
        {
            //System.out.println("Entered!");
            //add any code
        }

        @Override
        public void mouseClicked(MouseEvent e) 
        {
            //System.out.println("Clicked!");
            if(e.getSource().equals(lbl_stock))
            {
                System.out.println("Stock Clicked!");
                String fileName="Stock_List_"+ df.format(new Date())+".pdf";
                pdfWriter=new PDFWriter(fileName,"Stock Report");
                stockDAO=new StockDAO();
                try {
                    pdfWriter.printStockReport(stockDAO.getAllStockItems()) ;
                } catch (BadElementException ex) {
                    Logger.getLogger(ReportsDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(ReportsDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(e.getSource().equals(lbl_customer))
            {
                 System.out.println("Customer Clicked!");
                 String fileName="Customer_List_"+ df.format(new Date())+".pdf";
                 pdfWriter=new PDFWriter(fileName,"Customer Report");
                 custDAO=new CustomerDAO();
                try {
                    pdfWriter.printCustomerReport(custDAO.getAllCustomers()) ;
                } catch (BadElementException ex) {
                    Logger.getLogger(ReportsDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(ReportsDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                  
            }
            else if(e.getSource().equals(lbl_supplier))
            {
                 System.out.println("Supllier Clicked!");
                 String fileName="Supplier_List_"+ df.format(new Date())+".pdf";
                 pdfWriter=new PDFWriter(fileName,"Supplier Report");
                 supDAO=new SupplierDAO();
                try {
                    pdfWriter.printSupplierReport(supDAO.getAllSuppliers()) ;
                } catch (BadElementException ex) {
                    Logger.getLogger(ReportsDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(ReportsDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
     }
    
}
