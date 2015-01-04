/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.tables;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.lown.consultancy.accounts.ContraExpenses;
import org.lown.consultancy.accounts.Customer;
import org.lown.consultancy.accounts.ReportDescriptor;
import org.lown.consultancy.accounts.dao.ReportsDAO;

/**
 *
 * @author LENOVO USER
 */
public class ReportStatementTable extends JPanel{
    private JTable jTable;
    private String[] columnTitle=new String[]{"Date","Description","DR (Kes)","CR (Kes)","Balance (Kes)"};
    private static DefaultTableModel model ;
    private Object[][] data; 
    private ReportsDAO reportDao;
    private List<ReportDescriptor> report;
    public static ContraExpenses selectedRow;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private SimpleDateFormat dateF = new SimpleDateFormat("dd-MMM-yyyy");
    public static double selectedAmount;
    
    public ReportStatementTable()
    {
        model = new DefaultTableModel(data,columnTitle);
         
        reportDao=new ReportsDAO();         
        jTable = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
            return false; //Disallow the editing of any cell
            }
        };
        jTable.setSize(500, 200); 
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.setRowHeight(25);
        jTable.setBorder(BorderFactory.createTitledBorder(""));
        
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setFillsViewportHeight(true);
        JTableHeader header = jTable.getTableHeader();
        getSelectedRow();
        setPreferredSize(new Dimension(500, 200));       
        jTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        
        
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
                            Object data = model.getValueAt(selectedRow, 4);
                            int modelRow = jTable.convertRowIndexToModel(selectedRow);
                            System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                            System.out.println(String.format("Selected Row in view:  " + data.toString()));
                            
                            //selectedAccount=cs.getContraAccountByName(data.toString())   ;  
                            selectedAmount=Double.parseDouble(data.toString());
                            
                       }
                       else
                       {
                           selectedAmount=0.0;
                       }
                   }
                   
               });
    }
    
    public void insertCustomerStatement(Customer c)
    {
        //format columns
        //set coulmun size
        TableColumn column = null;
        column = jTable.getColumnModel().getColumn(0); //Id
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(1); //Code column
        column.setPreferredWidth(150);
        
        column = jTable.getColumnModel().getColumn(2); //Name column
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(3); //Name column
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(4); //Name column
        column.setPreferredWidth(100);        
       
        
        //set cell alignment
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);//amount
        jTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);//amount
        jTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);//amount
        
       
        
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
        
        report=reportDao.getCustomerReport(c);
        //list all available product categories from the database
        if(!report.isEmpty())
         {           
            for(ReportDescriptor r:report)
            {
                System.out.println(r.getTransactionDate()+"\t"+r.getDescription()+"\t"+r.getDrAmount()+"\t"+r.getCrAmount()+"\t"+r.getBalance());
                model.insertRow(jTable.getRowCount(),new Object[]{dateF.format(r.getTransactionDate()),r.getDescription(), df.format(r.getDrAmount()),df.format(r.getCrAmount()),df.format(r.getBalance())});
            }
          }

    }
    
}
