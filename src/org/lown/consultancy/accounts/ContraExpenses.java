/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts;

import java.util.Date;

/**
 *
 * @author LENOVO USER
 */
public class ContraExpenses {
    private int expense_id;
    private Date expenseDate;
    private String Description;
    private ContraAccount account;
    private double amount;
    private int pcv_id; //petty cash id

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public ContraAccount getAccount() {
        return account;
    }

    public void setAccount(ContraAccount account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPcv_id() {
        return pcv_id;
    }

    public void setPcv_id(int pcv_id) {
        this.pcv_id = pcv_id;
    }
    
    
    
    
            
}
