package com.aemcx.core.models;

public class Stripe {

    private String dropdownItem;
    private String itemPrice;
    private String itemDesc;

    public String getDropdownItem() {
        return dropdownItem;
    }
    public String getItemPrice(){
        return itemPrice;
    }
    public String getItemDesc() {
        return itemDesc;
    }
    
    
    public void setDropdownItem(String dropdownItem) {
        this.dropdownItem = dropdownItem;
    }
    public void setItemPrice(String itemPrice){
        this.itemPrice = itemPrice;
    }
    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    
   
}
