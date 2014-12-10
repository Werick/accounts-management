/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts;

/**
 *
 * @author LENOVO USER
 */
public class Category {
    private String categoryCode;
    private String categoryName;
    private int category_id;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Category other = (Category) obj;
        if ((this.categoryCode == null) ? (other.categoryCode != null) : !this.categoryCode.equals(other.categoryCode)) {
            return false;
        }
        if ((this.categoryName == null) ? (other.categoryName != null) : !this.categoryName.equals(other.categoryName)) {
            return false;
        }
        return true;
    }
    
    
    
}
