package com.isolutions.taxi5.APIAssist.Entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by fedar.trukhan on 06.10.16.
 */

@Root(name = "orders", strict=false)
public class AssistOrdersList {
    public List<AssistOrder> getOrders() {
        return orders;
    }

    @ElementList(name = "order", inline = true, required = true)
    private List<AssistOrder> orders;
}
