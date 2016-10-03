package com.isolutions.taxi5.APIAssist.Entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by fedar.trukhan on 03.10.16.
 */

@Root(name = "operation", strict=false)
public class AssistOperation {
    public String getBillnumber() {
        return billnumber;
    }

    public String getOperationstate() {
        return operationstate;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMeantypename() {
        return meantypename;
    }

    public String getMeansubtype() {
        return meansubtype;
    }

    public String getMeannumber() {
        return meannumber;
    }

    public String getCardholder() {
        return cardholder;
    }

    public String getCardexpirationdate() {
        return cardexpirationdate;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public String getCustomermessage() {
        return customermessage;
    }

    @Element(name = "billnumber", required = false)
    private String billnumber;

    @Element(name = "operationstate", required = false)
    private String operationstate;

    @Element(name = "amount", required = false)
    private Double amount;

    @Element(name = "currency", required = false)
    private String currency;

    @Element(name = "meantypename", required = false)
    private String meantypename;

    @Element(name = "meansubtype", required = false)
    private String meansubtype;

    @Element(name = "meannumber", required = false)
    private String meannumber;

    @Element(name = "cardholder", required = false)
    private String cardholder;

    @Element(name = "cardexpirationdate", required = false)
    private String cardexpirationdate;

    @Element(name = "responsecode", required = false)
    private String responsecode;

    @Element(name = "customermessage", required = false)
    private String customermessage;
}
