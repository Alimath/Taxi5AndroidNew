package com.isolutions.taxi5.APIAssist.Entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by fedar.trukhan on 03.10.16.
 */

@Root(name = "order", strict=false)
public class AssistOrder {
    public String getOrdernumber() {
        return ordernumber;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public String getMessage() {
        return message;
    }

    public String getOrdercomment() {
        return ordercomment;
    }

    public String getOrderdate() {
        return orderdate;
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

    public String getMeannumber() {
        return meannumber;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getIssuebank() {
        return issuebank;
    }

    public String getEmail() {
        return email;
    }

    public String getBankcountry() {
        return bankcountry;
    }

    public String getRate() {
        return rate;
    }

    public String getApprovalcode() {
        return approvalcode;
    }

    public String getMeansubtype() {
        return meansubtype;
    }

    public String getCardholder() {
        return cardholder;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public String getProtocoltypename() {
        return protocoltypename;
    }

    public String getTestmode() {
        return testmode;
    }

    public String getCustomermessage() {
        return customermessage;
    }

    public String getOrderstate() {
        return orderstate;
    }

    public String getProcessingname() {
        return processingname;
    }

    public String getOperationtype() {
        return operationtype;
    }

    public String getBillnumber() {
        return billnumber;
    }

    public Double getOrderamount() {
        return orderamount;
    }

    public String getOrdercurrency() {
        return ordercurrency;
    }

    public List<AssistOperation> getOperationList() {
        return operationList;
    }

    @Element(name = "ordernumber", required=false)
    private String ordernumber;

    @Element(name = "responsecode", required=false)
    private String responsecode;

    @Element(name = "recommendation", required=false)
    private String recommendation;

    @Element(name = "message", required=false)
    private String message;

    @Element(name = "ordercomment", required=false)
    private String ordercomment;

    @Element(name = "orderdate", required=false)
    private String orderdate;

    @Element(name = "amount", required=false)
    private Double amount;

    @Element(name = "currency", required=false)
    private String currency;

    @Element(name = "meantypename", required=false)
    private String meantypename;

    @Element(name = "meannumber", required=false)
    private String meannumber;

    @Element(name = "lastname", required=false)
    private String lastname;

    @Element(name = "firstname", required=false)
    private String firstname;

    @Element(name = "middlename", required=false)
    private String middlename;

    @Element(name = "issuebank", required=false)
    private String issuebank;

    @Element(name = "email", required=false)
    private String email;

    @Element(name = "bankcountry", required=false)
    private String bankcountry;

    @Element(name = "rate", required=false)
    private String rate;

    @Element(name = "approvalcode", required=false)
    private String approvalcode;

    @Element(name = "meansubtype", required=false)
    private String meansubtype;

    @Element(name = "cardholder", required=false)
    private String cardholder;

    @Element(name = "ipaddress", required=false)
    private String ipaddress;

    @Element(name = "protocoltypename", required=false)
    private String protocoltypename;

    @Element(name = "testmode", required=false)
    private String testmode;

    @Element(name = "customermessage", required=false)
    private String customermessage;

    @Element(name = "orderstate", required=false)
    private String orderstate;

    @Element(name = "processingname", required=false)
    private String processingname;

    @Element(name = "operationtype", required=false)
    private String operationtype;

    @Element(name = "billnumber", required=false)
    private String billnumber;

    @Element(name = "orderamount", required=false)
    private Double orderamount;

    @Element(name = "ordercurrency", required=false)
    private String ordercurrency;

    @ElementList(name = "operation", entry = "operation", inline = false, type = AssistOperation.class, required = false)
    private List<AssistOperation> operationList;

}
