package com.neeru.client.models;

import java.util.List;

/**
 * Created by brajendra on 13/07/17.
 */

public class Order {


    public int id;
    public int userId;
    public String status;
    public int slot;
    public String address;
    public String addressLine1;
    public String addressLine2;
    public String landmark;
    public int locationId;
    public double totalPrice;
    public String cancelledAt;
    public String cancelationReason;
    public String paymentMethod;
    public String expectedDeliveryDate;
    public String actualDeliveryDate;
    public String createdAt;
    public String updatedAt;

    public List<OrderItem> items;


}
