package com.example.recycledviewpoolexample.models;

import com.example.recycledviewpoolexample.helper.utils.PDFCreationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SubItemModel {
    public boolean swiped = false;
    private int subItemImage;
    private int subItemID;
    private String subItemTitle;
    private String subItemDesc;
    private boolean isPending;
    private boolean isReceived;

    public SubItemModel() {
    }

    public SubItemModel(int subItemID, String subItemTitle, String subItemDesc) {
        this.subItemID = subItemID;
        this.subItemTitle = subItemTitle;
        this.subItemDesc = subItemDesc;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }

    public static List<SubItemModel> createDummyPdfModel() {
        PDFCreationUtils.filePath.clear();
        PDFCreationUtils.progressCount = 1;

        boolean isFirstReceivedItem = false;
        List<SubItemModel> pdfModels = new ArrayList<>();
        for (int i = 0; i < 110; i++) {
            Random rand = new Random();
         //   int price = rand.nextInt((1000 - 200) + 1) + 200;

            SubItemModel model = new SubItemModel();
            if (!isFirstReceivedItem) {
                model.setReceived(true);
                isFirstReceivedItem = true;
            } else {
                model.setReceived(false);
            }

            model.setSubItemDesc("description");
//
            if (i % 4 == 0) {
                model.setSubItemTitle("Umesh Kumar " + i);
            } else {
                model.setSubItemTitle("Ram Kumar " + i);
            }
          //  model.setSubItemImage(i % 3);
            pdfModels.add(model);
        }

        return pdfModels;
    }

    public int getSubItemID() {
        return subItemID;
    }

    public void setSubItemID(int subItemID) {
        this.subItemID = subItemID;
    }

    public int getSubItemImage() {
        return subItemImage;
    }

    public void setSubItemImage(int subItemImage) {
        this.subItemImage = subItemImage;
    }

    public String getSubItemTitle() {
        return subItemTitle;
    }

    public void setSubItemTitle(String subItemTitle) {
        this.subItemTitle = subItemTitle;
    }

    public String getSubItemDesc() {
        return subItemDesc;
    }

    public void setSubItemDesc(String subItemDesc) {
        this.subItemDesc = subItemDesc;
    }
}
