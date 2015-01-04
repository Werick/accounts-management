/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.tables;

import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.lown.consultancy.accounts.GlobalProperty;
import org.lown.consultancy.accounts.Stock;
import org.lown.consultancy.accounts.dao.CompanyDAO;

/**
 *
 * @author LENOVO USER
 */
public class GPTable extends JPanel{
    private JTable jTable;
    private String[] columnTitle=new String[]{"Property","Propetry Value","Description"};
    private static DefaultTableModel model ;
    private Object[][] data; 
    private CompanyDAO companyDAO;
    private List<GlobalProperty> gpList;
    private List<GlobalProperty> updateGPList;
    
    public static GlobalProperty selectedGP;
    
    public GPTable()
    {
        companyDAO=new CompanyDAO();
        gpList=companyDAO.getGlobalPropertyList();
        
        model = new DefaultTableModel(data,columnTitle);
        jTable = new JTable(model);        
        jTable.setSize(600, 300); 
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.setRowHeight(25);
        jTable.setBorder(BorderFactory.createTitledBorder(""));
        
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setFillsViewportHeight(true);
        JTableHeader header = jTable.getTableHeader();
        getSelectedRow();
        setPreferredSize(new Dimension(600, 300));       
        jTable.setPreferredScrollableViewportSize(new Dimension(600, 300));
        
        //set coulmun size
        TableColumn column = null;
        column = jTable.getColumnModel().getColumn(0); //Id
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(1); //Code column
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(2); //Name column
        column.setPreferredWidth(150);
       
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
                            
                            selectedGP=companyDAO.getGlobalProperty(data.toString())   ;         
                            
                       }
                   }
                   
               });
    }
     
    public void insertRow()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
         
        //list all available Contra Expenses from the database
        if(!gpList.isEmpty())
         {
            for (GlobalProperty gp:gpList)
            {
                //System.out.println("Testing Stuff"+c.getCustomerNumber());
                model.insertRow(jTable.getRowCount(),new Object[]{gp.getProperty(),gp.getPropertyValue(),gp.getDescription()});
             }
          }

    }
    public void insertRow(GlobalProperty gp)
    {
        //remove all rows      
        //model.getDataVector().removeAllElements();
        jTable.repaint();   
         
        //list all available product categories from the database
        if(gp!=null)
         {
            //System.out.println("Testing Stuff"+c.getCustomerNumber());
            model.insertRow(jTable.getRowCount(),new Object[]{gp.getProperty(),gp.getPropertyValue(),gp.getDescription()});
             
          }

    }
    public int getRowCount()
    {
        return jTable.getRowCount();
    }
    
    public void updateGlobalProperty()
    {
        updateGPList=new ArrayList<GlobalProperty>();
        for(int i=0;i<getRowCount();i++)
        {
            int selectedRow=i;
            if ((selectedRow>=0)&&(jTable.getRowCount()>selectedRow))
            {
                GlobalProperty gp=new GlobalProperty();
                gp=gpList.get(i);
                Object property = model.getValueAt(selectedRow, 0);
                Object value = model.getValueAt(selectedRow, 1);
                Object descr = model.getValueAt(selectedRow, 2);
                Object data = model.getValueAt(selectedRow, 0);
                
                int modelRow = jTable.convertRowIndexToModel(selectedRow);
                System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                System.out.println(String.format("Selected Row in view:  " + data.toString()));

                gp.setProperty(property.toString());
                gp.setPropertyValue(value.toString());
                gp.setDescription(descr.toString());                
                updateGPList.add(gp);
            }
        }
        //Persist the updated stock List
        if(!updateGPList.isEmpty())
        {
            try {
                companyDAO.updateGlobalProperty(updateGPList);
            } catch (SQLException ex) {
                Logger.getLogger(StockItemTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
