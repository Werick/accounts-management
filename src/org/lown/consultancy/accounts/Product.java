/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts;

/**
 *
 * @author LENOVO USER
 */
public class Product {
    private int product_id;
    private String productName;
    private String productCode;
    private String measureUnit;
    private int category_id;
    private Category category;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
        final Product other = (Product) obj;
        if ((this.productName == null) ? (other.productName != null) : !this.productName.equals(other.productName)) {
            return false;
        }
        if ((this.productCode == null) ? (other.productCode != null) : !this.productCode.equals(other.productCode)) {
            return false;
        }
        if ((this.measureUnit == null) ? (other.measureUnit != null) : !this.measureUnit.equals(other.measureUnit)) {
            return false;
        }
        if (this.category_id != other.category_id) {
            return false;
        }
        return true;
    }   
    
    
}
