/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.dao.CompanyDAO;
import org.lown.consultancy.accounts.tables.GPTable;

/**
 *
 * @author LENOVO USER
 */
public class GPDialog extends JPanel implements ActionListener{
    
    private static final String ACT_BACK="close";
    private static final String ACT_UPDATE="update";
    public static final Font titleFont = new Font("Times New Roman", Font.BOLD, 14);
    
    private static JDialog dlgGP;
    
    private JButton btnClose;
    private JButton btnUpdate;    
    
    private JPanel pGlobalProperty;
    
    private TitledBorder titled1 = new TitledBorder("List of Global Properties");
    
    private CompanyDAO companyService;
    private GPTable gpTable;
    
    public GPDialog()
    {
        dlgGP= new JDialog((JDialog)null, "Manage Global Properties", true);
        dlgGP.setLayout(null);
        dlgGP.setSize(750, 500);//Width size, Height size
        dlgGP.setLocationRelativeTo(null);//center the invoice on the screen
        
        pGlobalProperty=new JPanel();
        pGlobalProperty.setBounds(20, 20, 700, 380);
        pGlobalProperty.setBorder(titled1);  
        pGlobalProperty.setLayout(null);
        dlgGP.add(pGlobalProperty);
        
         //Category List Table
        gpTable=new GPTable();
        gpTable.setBounds(20,20,650, 350);
        pGlobalProperty.add(gpTable);
        gpTable.insertRow();
        
        btnUpdate=new JButton("Update");
        btnUpdate.setBounds(20, 410, 150, 40);
        btnUpdate.setActionCommand(ACT_UPDATE);
        btnUpdate.addActionListener(this);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgGP.add(btnUpdate);
        
        btnClose=new JButton("Back");
        btnClose.setBounds(570, 410, 150, 40);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dlgGP.add(btnClose);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgGP.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_UPDATE))
        {
            gpTable.updateGlobalProperty();
	}
    }
    
    public static void createAndShowGUI()
    {
         AccountsManagement.logger.info("Loading Global Properties Dialog");
         GPDialog gpDialog = new GPDialog();      
         
         dlgGP.setVisible(true);          
         dlgGP.dispose(); //close the app once done
    }
}
