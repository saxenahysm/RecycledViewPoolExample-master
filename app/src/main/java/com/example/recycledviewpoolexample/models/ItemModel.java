package com.example.recycledviewpoolexample.models;

import java.util.List;

public class ItemModel {
    private String itemTitle;
    private int itemID;
    private List<SubItemModel> subItemModelList;
    private List<ComboModel> comboModelList;
    public boolean swiped = false;

    public ItemModel() {
    }

    public ItemModel(String itemTitle, int itemID, List<SubItemModel> subItemModelList) {
        this.itemTitle = itemTitle;
        this.itemID = itemID;
        this.subItemModelList = subItemModelList;
    }

    public ItemModel(String itemTitle, int itemID) {
        this.itemTitle = itemTitle;
        this.itemID = itemID;
    }

    public ItemModel(List<ComboModel> comboModelList) {
        this.comboModelList = comboModelList;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public List<SubItemModel> getSubItemModelList() {
        return subItemModelList;
    }

    public void setSubItemModelList(List<SubItemModel> subItemModelList) {
        this.subItemModelList = subItemModelList;
    }

    public List<ComboModel> getComboModelList() {
        return comboModelList;
    }

    public void setComboModelList(List<ComboModel> comboModelList) {
        this.comboModelList = comboModelList;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
}
