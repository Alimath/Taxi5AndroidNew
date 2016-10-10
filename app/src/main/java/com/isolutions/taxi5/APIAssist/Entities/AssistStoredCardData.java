package com.isolutions.taxi5.APIAssist.Entities;

import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by fedar.trukhan on 03.10.16.
 */

public class AssistStoredCardData {
    public String initBillNumber;
    public String currency;
    public String meanType;
    public String meanSubtype;
    public String meanNumber;
    public String cardHolder;
    public String cardExpDate;
    public String cardComment;
    public String initBillResponseCode;
    public Boolean isOneClickCard = false;

    public AssistStoredCardData() {
        
    }

    public AssistStoredCardData(AssistOrder order) {
        if(order != null) {
            if(!TextUtils.isEmpty(order.getBillnumber())) {
                this.initBillNumber = order.getBillnumber();
            }
            if(!TextUtils.isEmpty(order.getOrdercurrency())) {
                this.currency = order.getOrdercurrency();
            }
            if(order.getOperationList().size() > 0 ) {
                AssistOperation operation = order.getOperationList().get(0);
                if (!TextUtils.isEmpty(operation.getResponsecode())) {
                    this.initBillResponseCode = operation.getResponsecode();
                }
                if (!TextUtils.isEmpty(operation.getMeantypename())) {
                    this.meanType = operation.getMeantypename();
                }
                if (!TextUtils.isEmpty(operation.getMeannumber())) {
                    this.meanNumber = operation.getMeannumber();
                }
                if (!TextUtils.isEmpty(operation.getMeansubtype())) {
                    this.meanSubtype = operation.getMeansubtype();
                }
                if (!TextUtils.isEmpty(operation.getCardholder())) {
                    this.cardHolder = operation.getCardholder();
                }
                if (!TextUtils.isEmpty(operation.getCardexpirationdate())) {
                    this.cardExpDate = operation.getCardexpirationdate();
                }
            }
        }
    }

    public String getDescription() {
        return this.initBillNumber + ", " + this.currency + ", " + this.meanType + ", " +
                this.meanSubtype + ", " + this.meanNumber + ", " + cardHolder + ", " +
                this.cardExpDate + ", " + this.initBillResponseCode;
    }
}
