package com.isolutions.taxi5.APIAssist.Entities;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedar.trukhan on 03.10.16.
 */

@Root(name = "result", strict=false)
public class AssistOrderStatusResponseData {
    @Attribute(name = "firstcode")
    private Integer firstcode;

    @Attribute(name = "secondcode")
    private Integer secondcode;

    public Integer getCount() {
        return count;
    }

    public Integer getFirstcode() {
        return firstcode;
    }

    public Integer getSecondcode() {
        return secondcode;
    }

    @Attribute(name = "count")
    private Integer count;

    public List<AssistOrder> getOrdersList() {
        return ordersList;
    }

    @ElementList(name = "orders", entry = "order", inline = false, type = AssistOrder.class, required = true)
    private List<AssistOrder> ordersList;
}
