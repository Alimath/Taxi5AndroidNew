package com.isolutions.taxi5.APIAssist.Entities;


import io.paperdb.Paper;

/**
 * Created by fedar.trukhan on 03.10.16.
 */

public class AssistCustomerInfo {
    private static volatile AssistCustomerInfo instance;

    public static AssistCustomerInfo getInstance() {
        AssistCustomerInfo localInstance = instance;
        if(localInstance == null) {
            synchronized(AssistCustomerInfo.class) {
                localInstance = instance;
                if(localInstance == null) {
                    instance = localInstance = new AssistCustomerInfo();
                }
            }

        }

        localInstance.loadProfileData();


        return localInstance;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerFamilyName() {
        return customerFamilyName;
    }

    public void setCustomerFamilyName(String customerFamilyName) {
        this.customerFamilyName = customerFamilyName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    private String customerName;
    private String customerFamilyName;
    private String customerEmail;

    private void loadProfileData() {
        AssistCustomerInfo tData = (AssistCustomerInfo) Paper.book().read("taxi5AssistCustomerData");

        if(tData != null) {
            this.customerEmail = tData.customerEmail;
            this.customerName = tData.customerName;
            this.customerFamilyName = tData.customerFamilyName;
        }
        else {
            this.customerEmail = null;
            this.customerName = null;
            this.customerFamilyName = null;
        }
    }

    public void saveCustomerData() {
        Paper.book().write("taxi5AssistCustomerData", this);
    }

    public static void ClearCustomerData() {
        Paper.book().delete("taxi5AssistCustomerData");
    }
}
