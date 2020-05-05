package com.example.recycledviewpoolexample.models;

public class ComboModel {
    private int comboItemImage;
    private String comboItemTitle;
    private String comboItemDesc;

    public ComboModel(String comboItemTitle, String comboItemDesc) {
        this.comboItemTitle = comboItemTitle;
        this.comboItemDesc = comboItemDesc;
    }

    public int getComboItemImage() {
        return comboItemImage;
    }

    public void setComboItemImage(int comboItemImage) {
        this.comboItemImage = comboItemImage;
    }

    public String getComboItemTitle() {
        return comboItemTitle;
    }

    public void setComboItemTitle(String comboItemTitle) {
        this.comboItemTitle = comboItemTitle;
    }

    public String getComboItemDesc() {
        return comboItemDesc;
    }

    public void setComboItemDesc(String comboItemDesc) {
        this.comboItemDesc = comboItemDesc;
    }
}
