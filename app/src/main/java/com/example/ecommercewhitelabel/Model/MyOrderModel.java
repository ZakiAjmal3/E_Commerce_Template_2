package com.example.ecommercewhitelabel.Model;

public class MyOrderModel {
    String orderId,orderStatus,orderDate,orderProductTitle,orderPrice;

    public MyOrderModel(String orderId, String orderStatus, String orderDate, String orderProductTitle, String orderPrice) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderProductTitle = orderProductTitle;
        this.orderPrice = orderPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderProductTitle() {
        return orderProductTitle;
    }

    public void setOrderProductTitle(String orderProductTitle) {
        this.orderProductTitle = orderProductTitle;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }
}
